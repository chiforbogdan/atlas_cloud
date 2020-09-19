package ro.atlas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.atlas.dto.AtlasClientCommandDto;
import ro.atlas.dto.AtlasOwnerCommandDto;
import ro.atlas.entity.AtlasOwner;
import ro.atlas.repository.AtlasOwnerRepository;
import ro.atlas.service.AtlasGatewayService;
import ro.atlas.service.AtlasOwnerService;

@Component
public class AtlasOwnerServiceImpl implements AtlasOwnerService {

	private static final Logger LOG = LoggerFactory.getLogger(AtlasOwnerServiceImpl.class);

	private @Autowired AtlasOwnerRepository ownerRepository;

	private @Autowired AtlasGatewayService gatewayService;

	@Override
	public synchronized void enqueueOwnerCommand(String gatewayIdentity, String ownerIdentity,
			AtlasClientCommandDto clientCommand) {
		/* Sanity check */
		if (gatewayIdentity == null || gatewayIdentity.isEmpty()) {
			LOG.error("Empty gateway identity!");
			return;
		}
		if (ownerIdentity == null || ownerIdentity.isEmpty()) {
			LOG.error("Empty owner identity!");
			return;
		}
		if (clientCommand == null) {
			LOG.error("Empty client command!");
			return;
		}

		/* Save owner command to database */
		AtlasOwner owner = ownerRepository.findByOwnerIdentity(ownerIdentity);
		if (owner == null) {
			owner = new AtlasOwner();
			owner.setOwnerIdentity(ownerIdentity);
			owner.setOwnerCommands(new HashMap<String, LinkedList<AtlasClientCommandDto>>());
			owner = ownerRepository.insert(owner);
		}

		/* Add client command */
		if (owner.getOwnerCommands().get(gatewayIdentity) == null) {
			owner.getOwnerCommands().put(gatewayIdentity, new LinkedList<AtlasClientCommandDto>());
		}
		owner.getOwnerCommands().get(gatewayIdentity).add(clientCommand);
		owner.setNotifyUser(true);

		LOG.info("Save command for owner with identity {} and gateway with identity {}", ownerIdentity,
				gatewayIdentity);

		/* Save owner information */
		ownerRepository.save(owner);
	}

	@Override
	public synchronized HashMap<String, LinkedList<AtlasClientCommandDto>> fetchOwnerCommands(String ownerIdentity) {
		if (ownerIdentity == null || ownerIdentity.isEmpty()) {
			return null;
		}

		LOG.info("Fetch owner commands for owner with identity {}", ownerIdentity);

		AtlasOwner owner = ownerRepository.findByOwnerIdentity(ownerIdentity);
		if (owner == null) {
			LOG.error("Cannot find owner with identity {}", ownerIdentity);
			return null;
		}

		return owner.getOwnerCommands();
	}

	@Override
	public boolean setOwnerCommandStatus(String ownerIdentity, AtlasOwnerCommandDto ownerCommand) {
		if (ownerIdentity == null || ownerIdentity.isEmpty()) {
			LOG.error("Cannot process owner status command with empty owner identity!");
			return false;
		}

		if (ownerCommand == null) {
			LOG.error("Cannot process empty owner status command!");
			return false;
		}

		LOG.info("Set owner status command with sequence number {} for owner with identity {}", ownerCommand.getSeqNo(),
				ownerIdentity);

		AtlasOwner owner = ownerRepository.findByOwnerIdentity(ownerIdentity);
		if (owner == null) {
			LOG.error("Cannot find owner with identity {}", ownerIdentity);
			return false;
		}

		LinkedList<AtlasClientCommandDto> commands = owner.getOwnerCommands().get(ownerCommand.getGatewayIdentity());
		if (commands == null || commands.isEmpty()) {
			LOG.error("Cannot find command list for gateway with identity {}", ownerCommand.getGatewayIdentity());
			return false;
		}

		/*
		 * Get first client command from the gateway command list (owner MUST
		 * approve/reject the first command)
		 */
		AtlasClientCommandDto clientCommand = commands.get(0);
		/*
		 * If the owner sends an old command which already was approved, then
		 * acknowledge this as success
		 */
		if (ownerCommand.getSeqNo() < clientCommand.getSeqNo()) {
			LOG.info(
					"Command with sequence number {} for owner with identity {} and gateway with identity {} seems like an already processed command. Send success to owner...",
					ownerCommand.getSeqNo(), ownerIdentity, ownerCommand.getGatewayIdentity());
			return true;
		}

		if (clientCommand.getSeqNo() < ownerCommand.getSeqNo()) {
			LOG.error(
					"Status command for owner with identity {} and gateway with identity {} is be rejected: sequence number mismatch",
					ownerIdentity, ownerCommand.getGatewayIdentity());
			return false;
		}

		if (!clientCommand.getClientIdentity().equalsIgnoreCase(ownerCommand.getClientIdentity())) {
			LOG.error(
					"Status command for owner with identity {} and gateway with identity {} is be rejected: client identity mismatch",
					ownerIdentity, ownerCommand.getGatewayIdentity());
			return false;
		}
		
		/*
		 * If command is approved, then send it on the gateway side which in turn will
		 * send it to the client
		 */
		if (ownerCommand.isApproved()) {
			LOG.info(
					"Sending approved command with sequence number {} to gateway for owner with identity {} and gateway with identity {}",
					ownerCommand.getSeqNo(), ownerIdentity, ownerCommand.getGatewayIdentity());
			
			if (ownerCommand.getSignature() == null || ownerCommand.getSignature().isEmpty()) {
				LOG.info(
						"Approved command with sequence number {} to gateway for owner with identity {} and gateway with identity {} is rejected: empty signature",
						ownerCommand.getSeqNo(), ownerIdentity, ownerCommand.getGatewayIdentity());

				return false;
			}
			
			/* Remove command from owner list and move it to gateway list */
			clientCommand.setSignature(ownerCommand.getSignature());
			if (!gatewayService.sendApprovedCommandToClient(ownerCommand.getGatewayIdentity(), clientCommand)) {
				LOG.error(
						"Cannot insert approved command in the database for owner with identity {} and gateway with identity {}",
						ownerIdentity, ownerCommand.getGatewayIdentity());
				return false;
			}
		} else {
			LOG.info(
					"Deleting rejected command with sequence number {} for owner with identity {} and gateway with identity {}",
					ownerCommand.getSeqNo(), ownerIdentity, ownerCommand.getGatewayIdentity());
		}

		/* Remove command from the owner list */
		commands.remove(0);
		if (commands.isEmpty()) {
			owner.getOwnerCommands().remove(ownerCommand.getGatewayIdentity());
		}
		
		ownerRepository.save(owner);

		return true;
	}
}
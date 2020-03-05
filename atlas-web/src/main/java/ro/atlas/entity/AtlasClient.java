package ro.atlas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AtlasClient {
	@Id
	private String id;
	
	/* Client device identity */
	private String identity;

	/* Client device system info */
	private String registered;
	private String lastRegisterTime;
	private String lastKeepAliveTime;
	private String ipPort;
	
	/* Telemetry features */
	private String sysinfoLoad1;
	private String sysinfoLoad5;
	private String sysinfoLoad15;
	private String sysinfoFreehigh;
	private String sysinfoTotalhigh;
	private String sysinfoFreeswap;
	private String sysinfoBufferram;
	private String kernelInfo;//!!
	private String sysinfoProcs;
	private String sysinfoUptime;
	private String hostname;//!!!
	private String sysinfoFreeram;
	private String sysinfoTotalswap;
	private String sysinfoTotalram;
	private String sysinfoSharedram;
	private String packetsPerMinute;//!!!
	private String packetsAvgLength;//!!!
	
	public String getSysinfoLoad1() {
		return sysinfoLoad1;
	}
	
	public void setSysinfoLoad1(String sysinfoLoad1) {
		this.sysinfoLoad1 = sysinfoLoad1;
	}
	
	public String getSysinfoLoad5() {
		return sysinfoLoad5;
	}
	public void setSysinfoLoad5(String sysinfoLoad5) {
		this.sysinfoLoad5 = sysinfoLoad5;
	}
	
	public String getSysinfoLoad15() {
		return sysinfoLoad15;
	}
	
	public void setSysinfoLoad15(String sysinfoLoad15) {
		this.sysinfoLoad15 = sysinfoLoad15;
	}
	
	public String getSysinfoFreehigh() {
		return sysinfoFreehigh;
	}
	
	public void setSysinfoFreehigh(String sysinfoFreehigh) {
		this.sysinfoFreehigh = sysinfoFreehigh;
	}
	
	public String getSysinfoTotalhigh() {
		return sysinfoTotalhigh;
	}
	
	public void setSysinfoTotalhigh(String sysinfoTotalhigh) {
		this.sysinfoTotalhigh = sysinfoTotalhigh;
	}
	public String getSysinfoFreeswap() {
		return sysinfoFreeswap;
	}
	
	public void setSysinfoFreeswap(String sysinfoFreeswap) {
		this.sysinfoFreeswap = sysinfoFreeswap;
	}
	
	public String getSysinfoBufferram() {
		return sysinfoBufferram;
	}
	
	public void setSysinfoBufferram(String sysinfoBufferram) {
		this.sysinfoBufferram = sysinfoBufferram;
	}
	
	public String getKernelInfo() {
		return kernelInfo;
	}
	public void setKernelInfo(String kernelInfo) {
		this.kernelInfo = kernelInfo;
	}
	public String getSysinfoProcs() {
		return sysinfoProcs;
	}
	
	public void setSysinfoProcs(String sysinfoProcs) {
		this.sysinfoProcs = sysinfoProcs;
	}
	
	public String getSysinfoUptime() {
		return sysinfoUptime;
	}
	
	public void setSysinfoUptime(String sysinfoUptime) {
		this.sysinfoUptime = sysinfoUptime;
	}
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getSysinfoFreeram() {
		return sysinfoFreeram;
	}
	
	public void setSysinfoFreeram(String sysinfoFreeram) {
		this.sysinfoFreeram = sysinfoFreeram;
	}
	
	public String getSysinfoTotalswap() {
		return sysinfoTotalswap;
	}
	
	public void setSysinfoTotalswap(String sysinfoTotalswap) {
		this.sysinfoTotalswap = sysinfoTotalswap;
	}
	
	public String getSysinfoTotalram() {
		return sysinfoTotalram;
	}
	
	public void setSysinfoTotalram(String sysinfoTotalram) {
		this.sysinfoTotalram = sysinfoTotalram;
	}
	
	public String getSysinfoSharedram() {
		return sysinfoSharedram;
	}
	
	public void setSysinfoSharedram(String sysinfoSharedram) {
		this.sysinfoSharedram = sysinfoSharedram;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public void updateInfo(AtlasClient clientInfo) {
		if (clientInfo == null)
			return;

		if (clientInfo.getRegistered() != null)
			this.setRegistered(clientInfo.getRegistered());
		
		if (clientInfo.getLastRegisterTime() != null)
			this.setLastRegisterTime(clientInfo.getLastRegisterTime());

		if (clientInfo.getLastKeepAliveTime() != null)
			this.setLastKeepAliveTime(clientInfo.getLastKeepAliveTime());
		
		if (clientInfo.getIpPort() != null)
			this.setIpPort(clientInfo.getIpPort());
		
		if (clientInfo.getSysinfoLoad1() != null)
			this.setSysinfoLoad1(clientInfo.getSysinfoLoad1());
		
		if (clientInfo.getSysinfoLoad5() != null)
			this.setSysinfoLoad5(clientInfo.getSysinfoLoad5());
		
		if (clientInfo.getSysinfoLoad15() != null)
			this.setSysinfoLoad15(clientInfo.getSysinfoLoad15());
		
		if (clientInfo.getSysinfoFreehigh() != null)
			this.setSysinfoFreehigh(clientInfo.getSysinfoFreehigh());
		
		if (clientInfo.getSysinfoTotalhigh() != null)
			this.setSysinfoTotalhigh(clientInfo.getSysinfoTotalhigh());
		
		if (clientInfo.getSysinfoFreeswap() != null)
			this.setSysinfoFreeswap(clientInfo.getSysinfoFreeswap());
		
		if (clientInfo.getSysinfoBufferram() != null)
			this.setSysinfoBufferram(clientInfo.getSysinfoBufferram());

		if (clientInfo.getKernelInfo() != null)
			this.setKernelInfo(clientInfo.getKernelInfo());
		
		if (clientInfo.getSysinfoProcs() != null)
			this.setSysinfoProcs(clientInfo.getSysinfoProcs());
		
		if (clientInfo.getSysinfoUptime() != null)
			this.setSysinfoUptime(clientInfo.getSysinfoUptime());

		if (clientInfo.getHostname() != null)
			this.setHostname(clientInfo.getHostname());
		
		if (clientInfo.getSysinfoFreeram() != null)
			this.setSysinfoFreeram(clientInfo.getSysinfoFreeram());

		if (clientInfo.getSysinfoTotalswap() != null)
			this.setSysinfoTotalswap(clientInfo.getSysinfoTotalswap());
		
		if (clientInfo.getSysinfoTotalram() != null)
			this.setSysinfoTotalram(clientInfo.getSysinfoTotalram());
		
		if (clientInfo.getSysinfoSharedram() != null)
			this.setSysinfoSharedram(clientInfo.getSysinfoSharedram());
		
		if (clientInfo.getPacketsPerMinute() != null)
			this.setPacketsPerMinute(clientInfo.getPacketsPerMinute());
		
		if (clientInfo.getPacketsAvgLength() != null)
			this.setPacketsAvgLength(clientInfo.getPacketsAvgLength());

	}

	public String getLastRegisterTime() {
		return lastRegisterTime;
	}

	public void setLastRegisterTime(String lastRegisterTime) {
		this.lastRegisterTime = lastRegisterTime;
	}

	public String getLastKeepAliveTime() {
		return lastKeepAliveTime;
	}

	public void setLastKeepAliveTime(String lastKeepAliveTime) {
		this.lastKeepAliveTime = lastKeepAliveTime;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public String getPacketsPerMinute() {
		return packetsPerMinute;
	}

	public void setPacketsPerMinute(String packetsPerMinute) {
		this.packetsPerMinute = packetsPerMinute;
	}

	public String getPacketsAvgLength() {
		return packetsAvgLength;
	}

	public void setPacketsAvgLength(String packetsAvgLength) {
		this.packetsAvgLength = packetsAvgLength;
	}

	public String getRegistered() {
		return registered;
	}

	public void setRegistered(String registered) {
		this.registered = registered;
	}


}

package ro.atlas.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ro.atlas.entity.AtlasGateway;

public interface AtlasGatewayRepository extends MongoRepository <AtlasGateway, String> {
	AtlasGateway findByPsk(String psk);
}

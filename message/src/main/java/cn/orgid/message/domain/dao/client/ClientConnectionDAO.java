package cn.orgid.message.domain.dao.client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.client.ClientConnection;

public interface ClientConnectionDAO extends JpaRepository<ClientConnection, Long> {

	ClientConnection findByClientIdAndConnectionTokenAndConnectionState(Long clientId, String connectionToken, String connectionState);

	List<ClientConnection>  findByClientId(Long clientId);
	
	List<ClientConnection> findByClientIdAndConnectionState(Long clientId,String connectionState);

	List<ClientConnection> findByClientTag(String clientTag);

	List<ClientConnection> findByAppIdAndClientTag(String appId,String toClientTag);

	List<ClientConnection> findByClientTagAndConnectionState(String clientTag, String connectionState);

	List<ClientConnection> findByConnectionState(String connectionState);

}

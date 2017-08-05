package cn.orgid.message.domain.dao.client;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.client.Client;

public interface ClientDAO extends JpaRepository<Client, Long> {
	
	Client findByToken(String connectionToken);

}

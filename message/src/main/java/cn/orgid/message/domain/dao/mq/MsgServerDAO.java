package cn.orgid.message.domain.dao.mq;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.mq.MsgServer;

public interface MsgServerDAO extends JpaRepository<MsgServer, Long>{

	MsgServer findByServerId(String serverId);

}

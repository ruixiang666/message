package cn.orgid.message.domain.dao.mq;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.mq.MsgQueue;

public interface MsgQueueDAO extends JpaRepository<MsgQueue, Long>{

}

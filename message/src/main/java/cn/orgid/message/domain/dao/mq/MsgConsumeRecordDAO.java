package cn.orgid.message.domain.dao.mq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.orgid.message.domain.model.mq.MsgConsumeRecord;

public interface MsgConsumeRecordDAO extends JpaRepository<MsgConsumeRecord, Long> {

	
	

	MsgConsumeRecord findByMsgIdAndSubscriberId(Long id, Long id2);

	List<MsgConsumeRecord> findBySubscriberId(Long serverId);

	List<MsgConsumeRecord> findBySubscriberIdAndConsumed(Long id, boolean b);

}

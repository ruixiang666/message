package cn.orgid.message.domain.dao.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.orgid.message.domain.model.message.NotifyMessage;

public interface NotifyMessageDAO extends JpaRepository<NotifyMessage, Long> {

	@Query("select m from NotifyMessage m where m.sendCount=0 and m.retryCount<60 ")
	List<NotifyMessage> findMessageToSend();

}

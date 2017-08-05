package cn.orgid.message.domain.dao.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import cn.orgid.message.domain.model.message.ReliableMessage;

public interface ReliableMessageDAO extends JpaRepository<ReliableMessage, Long>,JpaSpecificationExecutor<ReliableMessage>{

	List<ReliableMessage> findByAcknowledge(boolean b);

	ReliableMessage findByMsgKey(String key);

	List<ReliableMessage> findByToClientIdAndAcknowledge(Long clientId, boolean b);

	List<ReliableMessage> findByToClientId(String clientId);

	@Transactional
	@Modifying
	@Query("delete from ReliableMessage  t where t.msgKey=?1")
	int deleteByMsgKey(String key);

	
	

}

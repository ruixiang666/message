package cn.orgid.message.domain.dao.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.message.ReportMessage;

public interface ReportMessageDAO  extends JpaRepository<ReportMessage, Long>{

	

	List<ReportMessage> findTop200ByOrderByIdAsc();

}

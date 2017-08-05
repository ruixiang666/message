package cn.orgid.message.domain.dao.file;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.file.UploadFile;

public interface UploadFileDAO extends JpaRepository<UploadFile, Long>{

}

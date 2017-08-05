package cn.orgid.message.domain.dao.file;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.orgid.message.domain.model.file.FileAccessKey;

public interface FileAccessKeyDAO extends JpaRepository<FileAccessKey,Long> {

	FileAccessKey findByAccessKey(String accessKey);
	
}

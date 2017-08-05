package cn.orgid.message.domain.model.file;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import cn.orgid.message.domain.model.EntityBase;

@Entity
@Table(name="t_upload_file")
public class UploadFile extends EntityBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String uri;
	
	private Date uploadTime;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
}

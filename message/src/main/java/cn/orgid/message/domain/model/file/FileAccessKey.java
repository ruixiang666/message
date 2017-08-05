package cn.orgid.message.domain.model.file;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Table;
import cn.orgid.message.domain.model.EntityBase;


@Entity
@Table(name="t_file_access_key")
public class FileAccessKey extends EntityBase {
	
	private static final long serialVersionUID = 1L;
	
	private String accessKey;
	
	private boolean valid;
	

	public String getAccessKey() {
	
		return accessKey;
	
	}

	public void setAccessKey(String accessKey) {
	
		this.accessKey = accessKey;
		
	}
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void initAccessKey(){
		
		this.accessKey=UUID.randomUUID().toString().replaceAll("-", "");
		
	}
	
}

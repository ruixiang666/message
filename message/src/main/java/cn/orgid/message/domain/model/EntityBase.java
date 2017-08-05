package cn.orgid.message.domain.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@MappedSuperclass
public class EntityBase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;

	@Version
	private Integer version;

	/**
	 * Gets the unique identifier for this relation.
	 *
	 * @return The unique identifier for this relation.
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}

package cn.orgid.message.domain.model.message;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Notify")
public class NotifyMessage extends ReliableMessage implements Reliable {

	private static final long serialVersionUID = 1L;

}

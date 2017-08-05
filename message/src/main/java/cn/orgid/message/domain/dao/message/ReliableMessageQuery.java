package cn.orgid.message.domain.dao.message;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import cn.orgid.message.domain.model.message.ReliableMessage;

public class ReliableMessageQuery {
	
	
	
	public static Specification<ReliableMessage> queryReliableMessageSpecification(final Integer retryCount) {

		return new Specification<ReliableMessage>() {
			public Predicate toPredicate(Root<ReliableMessage> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Predicate p = cb.lessThan(root.get("retryCount").as(Integer.class),retryCount);
				predicates.add(p);
				Predicate p1 = cb.and(predicates.toArray(new Predicate[predicates.size()]));
				return p1;
			}			
		};

	}

}

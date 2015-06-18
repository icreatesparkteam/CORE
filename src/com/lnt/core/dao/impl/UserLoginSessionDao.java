package com.lnt.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.lnt.core.model.UserLoginSession;

@Component
public class UserLoginSessionDao extends
		AbstractAppDao<UserLoginSession, String> {

	public UserLoginSession findBySessionId(String sessionId) {
		Criterion criterion = Restrictions.eq("sessionId", sessionId);
		List<UserLoginSession> list = findByCriteria(criterion);

		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
}

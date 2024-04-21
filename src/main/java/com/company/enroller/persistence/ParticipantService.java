package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
		String hql = "FROM Participant";

		if (key != null && !key.isEmpty()) {
			hql += " WHERE login LIKE '%" + key + "%'";
		}

		if ("login".equals(sortBy)) {
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				hql += " ORDER BY login " + sortOrder;
			} else {
				hql += " ORDER BY login ASC";
			}
		}

		Session session = connector.getSession();
		Query query = session.createQuery(hql);

		return query.list();
	}

	public Participant getByLogin(String login) {
		String hql = "FROM Participant WHERE login = :login";
		Query <Participant>query = connector.getSession().createQuery(hql, Participant.class);
		query.setParameter("login", login);

		return query.uniqueResult();
	}

	public void create(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void remove(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().remove(participant);
		transaction.commit();
	}

	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(participant);
		transaction.commit();
	}
}

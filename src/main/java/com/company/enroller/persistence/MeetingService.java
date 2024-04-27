package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll(String sortBy, String sortOrder, String key) {
		String hql = "FROM Meeting";

		if (key != null && !key.isEmpty()) {
			hql += " WHERE title LIKE '%" + key + "%'";
		}

		if ("title".equals(sortBy)) {
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				hql += " ORDER BY title " + sortOrder;
			} else {
				hql += " ORDER BY title ASC";
			}
		}

		if ("description".equals(sortBy)) {
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				hql += " ORDER BY description " + sortOrder;
			} else {
				hql += " ORDER BY description ASC";
			}
		}

		Session session = connector.getSession();
		Query query = session.createQuery(hql);

		return query.list();
	}

	public Meeting getById(long id) {
		String hql = "FROM Meeting WHERE id = :id";
		Query <Meeting>query = connector.getSession().createQuery(hql, Meeting.class);
		query.setParameter("id", id);

		return query.uniqueResult();
	}

	public void create(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void remove(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().remove(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}
}

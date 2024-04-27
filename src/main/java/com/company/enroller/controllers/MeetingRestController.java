package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Collection<Meeting>> getAll(
			@RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder,
			@RequestParam(value = "key", required = false) String key) {

		Collection<Meeting> meetings = meetingService.getAll(sortBy, sortOrder, key);

		return new ResponseEntity<>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingById(@PathVariable("id") long id) {
		Meeting meeting = meetingService.getById(id);

		if (meeting == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
		Meeting checkMeeting = meetingService.getById(meeting.getId());

		if (checkMeeting != null) {
			return new ResponseEntity<>("Unable to create. A meeting with id "
					+ meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}

		meetingService.create(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.getById(id);

		if (meeting == null) {
			return new ResponseEntity<>("Unable to delete, meeting doesn't exist",
					HttpStatus.NOT_FOUND);
		}

		meetingService.remove(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id,
											   @RequestBody Meeting meeting) {
		Meeting existingMeeting = meetingService.getById(id);

		if (existingMeeting == null) {
			return new ResponseEntity<>("Unable to update, meeting doesn't exist",
					HttpStatus.NOT_FOUND);
		}

		if (meeting.getDate() != null) {
			existingMeeting.setDate(meeting.getDate());
		}
		if (meeting.getDescription() != null) {
			existingMeeting.setDescription(meeting.getDescription());
		}
		if (meeting.getTitle() != null) {
			existingMeeting.setTitle(meeting.getTitle());
		}
		meetingService.update(existingMeeting);
		return new ResponseEntity<>(existingMeeting, HttpStatus.OK);
	}
}
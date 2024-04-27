package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Collection<Participant>> getAll(
			@RequestParam(value = "sortBy", defaultValue = "login") String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder,
			@RequestParam(value = "key", required = false) String key) {

		Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);

		return new ResponseEntity<>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{login}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("login") String login) {
		Participant participant = participantService.getByLogin(login);

		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createParticipant(@RequestBody Participant participant) {
		Participant checkParticipant = participantService.getByLogin(participant.getLogin());

		if (checkParticipant != null) {
			return new ResponseEntity<>("Unable to create. A participant with login "
					+ participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}

		String hashedPassword = passwordEncoder.encode(participant.getPassword());
		participant.setPassword(hashedPassword);

		participantService.create(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeParticipant(@PathVariable("login") String login) {
		Participant participant = participantService.getByLogin(login);

		if (participant == null) {
			return new ResponseEntity<>("Unable to delete, participant doesn't exist",
					HttpStatus.NOT_FOUND);
		}

		participantService.remove(participant);
		return new ResponseEntity<Participant>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{login}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("login") String login,
											   @RequestBody Participant participant) {
		Participant existingParticipant = participantService.getByLogin(login);

		if (existingParticipant == null) {
			return new ResponseEntity<>("Unable to update, participant doesn't exist",
					HttpStatus.NOT_FOUND);
		}

		existingParticipant.setPassword(participant.getPassword());
		participantService.update(existingParticipant);
		return new ResponseEntity<>(existingParticipant, HttpStatus.OK);
	}
}

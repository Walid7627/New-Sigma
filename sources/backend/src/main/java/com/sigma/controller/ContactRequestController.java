package com.sigma.controller;

import com.sigma.dto.ContactRequestDto;
import com.sigma.model.ContactRequest;
import com.sigma.repository.ContactRequestRepository;
import com.sigma.service.impl.EmailServiceImpl;
import com.sigma.util.IterableToList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/contacts")
public class ContactRequestController {

	private static final String MAIL_ADMIN = "Hubert.Dugas@rouen.fr";
	
	@GetMapping
	@ResponseBody
	public String list() {
		try {
			List<ContactRequest> cr = IterableToList.toList(contactRequestRepository.findAll());
			String jsonString = objectMapper.writeValueAsString(cr);
			return jsonString;
		}
		catch (Exception ex) {
			return "Error retrieving contact requests: " + ex.toString();
		}
	}

	@PostMapping
	@ResponseBody
	public String create(@RequestBody ContactRequestDto cr) {
		String result = "";
		try {
			ContactRequest contactRequest = new ContactRequest(cr.getNom(), 
					cr.getTelephone(),
					cr.getMail(),
					cr.getMessage());

			contactRequestRepository.save(contactRequest);
			String text = "<b>Nom: </b>" + cr.getNom() + "<br/>";
			text += "<b>Société: </b>" + cr.getNomSociete() + "<br/>";
			text += "<b>Numero Siret: </b>" + cr.getNumSiret() + "<br/>";
			text += "<b>Email: </b>" + cr.getMail() + "<br/>";
			text += (cr.getTelephone() != null && !cr.getTelephone().isEmpty()) ? "<b>Telephone: </b>" + cr.getTelephone() + "<br/>" : "";
			text += "<br/><b>Message: </b>" + cr.getMessage() + "<br/>";
			emailService.sendSimpleMessage(MAIL_ADMIN, "Contact SIGMA", text);
			result = String.valueOf(contactRequest.getId());
		} catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}

		return "Successfully added contact request (id:" + result + ")";
	}

	@GetMapping("/search")
	@ResponseBody
	public String find(@RequestParam(value="id") String id) {
		try {
			ContactRequest cr = contactRequestRepository.findOne(Long.parseLong(id));
			String jsonString = objectMapper.writeValueAsString(cr);
			return jsonString;
		} catch (Exception ex) {
			return "Error while finding contact request: " + ex.toString();
		}
	}

	@DeleteMapping
	@ResponseBody
	public String delete(@RequestParam(value="id") String id) {
		try {
			ContactRequest cr = contactRequestRepository.findOne(Long.parseLong(id));
			contactRequestRepository.delete(cr);
		} catch (Exception ex) {
			return "Error while trying to delete user (id: " + id + "): " + ex.toString();
		}
		return "Successfully deleted contact request (id: " + id + ")";
	}


	@Autowired
	private ContactRequestRepository contactRequestRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	EmailServiceImpl emailService;
}

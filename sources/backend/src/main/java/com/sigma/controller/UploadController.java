package com.sigma.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.config.JwtTokenUtil;
import com.sigma.model.ApiResponse;
import com.sigma.model.Document;
import com.sigma.model.Fournisseur;
import com.sigma.model.Privilege;
import com.sigma.repository.DocumentRepository;
import com.sigma.repository.FournisseurRepository;
import com.sigma.service.StorageService;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;

// import com.sigma.model.Document;

@RestController
@Controller
@RequestMapping("/api/files")
public class UploadController {

	@Autowired
	StorageService storageService;

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Autowired
	FournisseurRepository fournisseurRepository;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	JwtTokenUtil tokenUtil;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	List<String> files = new ArrayList<String>();

	@PostMapping
	@ResponseBody
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("user") String userMail,
			@RequestParam("type") String type)
					throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("fileUpload invoked.\nParameters:\nmail: " + userMail + "\ntype: " + type);

		String message = "";
		try {
			Fournisseur usr = fournisseurRepository.findByMail(userMail);
			if (usr == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"User " + userMail + " does not exist")
						);
			}
			
			String url = storageService.store(usr.getId(), file);
			if (type.equals("TYPE_DOCUMENT")) {
				Document document = new Document(url, this.getNameFromPath(url));
				documentRepository.save(document);
				usr.addDocument(document);
			} else if(type.equals("TYPE_LOGO")) {
				if (usr.getLogo() != null && !usr.getLogo().isEmpty()) {
					try {
						storageService.deleteFile(usr.getLogo());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("upload TYPE_LOGO called :");
				usr.setLogo(url);
				System.out.println("\n\nUser logo url:" + usr.getLogo());
			} else {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Feature not yet implemented")
						);
			}

			fournisseurRepository.save(usr);


			message = usr.getLogo();
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							message)
					);
		} catch (IOException e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"IOException",
							e)
					);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							message,
							e)
					);
		}
	}

	// TODO: Check if user sending the request has correct role before deleting file
	@PostMapping("/delete")
	@ResponseBody
	public String deleteFile(HttpServletRequest request,
			@RequestParam("path") String path,
			@RequestParam("user") String userMail)
					throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			String header = request.getHeader("Authorization");

			if (header == null || !header.startsWith("Bearer ")) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Aucune session active. Veuillez vous reconnecter!")
						);
			}

			String authToken = header.substring(7);
			String userName = tokenUtil.getUsernameFromToken(authToken);

			System.out.println("Found username " + userName + " in token");


			Fournisseur usr = fournisseurRepository.findByMail(userMail);

			if (usr == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Veuillez vous reconnecter!")
						);
			}

			String name = this.getNameFromPath(path);
			Document doc = usr.getDocument(name);

			if (doc == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Document not found")
						);
			}

			usr.removeDocument(doc);
			fournisseurRepository.save(usr);
			documentRepository.delete(doc);
			storageService.delete(usr.getId(), path);

		} catch (FileNotFoundException e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"File not found",
							e)
					);
		} catch (IOException e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"IOException",
							e)
					);
		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Error",
							e)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"File deleted successfully")
				);
	}

	// TODO: Check auth token for user
	@PostMapping("/get")
	@ResponseBody
	public String getFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("path") String path)
	// public String getFile(HttpServletRequest request, @RequestParam("path") String path)
			throws com.fasterxml.jackson.core.JsonProcessingException {

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Aucune session active. Veuillez vous reconnecter!")
					);
		}

		String authToken = header.substring(7);
		String userName = tokenUtil.getUsernameFromToken(authToken);
		
		Utilisateur user = utilisateurRepository.findByMail(userName);

		if (user == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"User found in token does not exist")
					);
		}

		if (user.getRole() == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Insufficient privileges")
					);
		}

		if (user.getRole().getName().equals("ROLE_FOURNISSEUR")) {
			Fournisseur f = (Fournisseur) user;
			List<Document> documents = f.getDocuments()
					.stream()
					.filter(d -> d.getUrl().equals(path))
					.collect(Collectors.toList());

			if (documents.size() == 0 && !path.equals(f.getLogo())) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"You do not own this document and do not have the sufficient privileges to see it")
						);
			}
		}

		if (!user.getRole().getName().equals("ROLE_FOURNISSEUR")
				&& !user.getRole().getPrivileges().contains(Privilege.GET_OTHER_DOCUMENT)) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Insufficient privileges")
					);
		}


		File file = storageService.get(path);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());

			if (mimeType == null) {
				if (file.getName().endsWith("xlsx")) {
					mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
				} else {
					//unknown mimetype so set the mimetype to application/octet-stream
					mimeType = "application/octet-stream";
				}

			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 *
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			//Here we have mentioned it to show as attachment
			//response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			try {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());

				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.OK,
								"OK")
						);

			} catch (IOException e) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Error while copying stream",
								e)
						);
			}
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.EXPECTATION_FAILED,
						"File does not exist")
				);
	}


	//TODO: Check auth token for user
	@PostMapping("/logo")
	@ResponseBody
	public String getLogo(HttpServletRequest request, HttpServletResponse response, @RequestParam("path") String path)
	// public String getFile(HttpServletRequest request, @RequestParam("path") String path)
			throws com.fasterxml.jackson.core.JsonProcessingException {

		File file = storageService.get(path);
		if (file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());

			if (mimeType == null) {

				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 *
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			//Here we have mentioned it to show as attachment
			//response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			try {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());

				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.OK,
								"OK")
						);

			} catch (IOException e) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Error while copying stream",
								e)
						);
			}
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.EXPECTATION_FAILED,
						"Logo does not exist")
				);
	}

	//TODO: Check auth token for user


	// @GetMapping("/getall")
	// public ResponseEntity<List<String>> getListFiles(Model model) {
	// 	List<String> fileNames = files
	// 			.stream().map(fileName -> MvcUriComponentsBuilder
	// 					.fromMethodName(UploadController.class, "getFile", fileName).build().toString())
	// 			.collect(Collectors.toList());
	//
	// 	return ResponseEntity.ok().body(fileNames);
	// }
	//
	// // @GetMapping("/files/{filename:.+}")
	// // @ResponseBody
	// // public ResponseEntity<Resource> getFile(@PathVariable String filename) {
	// // 	Resource file = storageService.loadFile(filename);
	// // 	return ResponseEntity.ok()
	// // 			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
	// // 			.body(file);
	// // }
	//
	@PutMapping("/init")
	public String initFiles() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			storageService.init();
		} catch(Exception e) {
			String message = "Error while trying to initialize file folders";
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							message)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"File folders initialized successfully")
				);
	}

	private String getNameFromPath(String path) {
		return path.substring(path.lastIndexOf("/") + 1);
	}
}

package com.sigma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
	A class that contains utilities to store, replace, delete and get files
*/

@Service
public class StorageService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("files");

	public String store(Long id, MultipartFile file) throws IOException {
		if (!Files.exists(this.rootLocation.resolve(id.toString()))) {
			Files.createDirectory(this.rootLocation.resolve(id.toString()));
		}
		if (Files.exists(this.rootLocation.resolve(id + "/" + file.getOriginalFilename()))) {
			Files.delete(this.rootLocation.resolve(id + "/" + file.getOriginalFilename()));
		}
		Files.copy(file.getInputStream(), this.rootLocation.resolve(id + "/" + file.getOriginalFilename()));

		Path result = this.rootLocation.resolve(id + "/" + file.getOriginalFilename());
		return result.toString();
	}

	public String replace(Long id, MultipartFile file) throws IOException {
		if (Files.exists(this.rootLocation.resolve(id + "/" + file.getOriginalFilename()))) {
			Files.delete(this.rootLocation.resolve(id + "/" + file.getOriginalFilename()));
		}

		return store(id, file);
	}

	public void delete(Long id, String path) throws IOException {
		if (Files.exists(Paths.get(path))) {
			Files.delete(Paths.get(path));

			Path userDirectory = this.rootLocation.resolve(id.toString());

			if (Files.list(userDirectory).count() == 0) {
				Files.delete(userDirectory);
			}
		} else {
			throw new FileNotFoundException();
		}
	}
	
	public void deleteFile(String path) throws IOException {
		if (Files.exists(Paths.get(path))) {
			Files.delete(Paths.get(path));

		} else {
			throw new FileNotFoundException();
		}
	}

	public File get(String path) {
		// try {
		// 	Path file = Paths.get(path);
		// 	Resource resource = new UrlResource(file.toUri());
		// 	if (resource.exists() || resource.isReadable()) {
		// 		return resource;
		// 	} else {
		// 		throw new RuntimeException("FAIL!");
		// 	}
		// } catch (MalformedURLException e) {
		// 	throw new RuntimeException("FAIL!");
		// }
		return new File(path);
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public void init() {
		try {
			if (!Files.exists(rootLocation)) {
				Files.createDirectory(rootLocation);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}

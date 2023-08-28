package com.spring.mynotes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.mynotes.model.Note;
import com.spring.mynotes.model.User;
import com.spring.mynotes.service.NoteService;
import com.spring.mynotes.service.UserService;
import com.spring.mynotes.util.CookieManager;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@EntityScan("com.spring.mynotes.model")
public class AppController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NoteService noteService;

	private static final String COOKIE_NAME = "loggedInUser";
	private static final String UPLOAD_DIR = "Uploads";
	
	@GetMapping("/")
	public String welcomePage(HttpServletRequest request) {
		
		// handle the cookie session for already logged in user
		String cookieVal = CookieManager.getCookie(request, COOKIE_NAME);
		if(!cookieVal.equals("")) return "redirect:/home";
		return "login";
	}
	
	
	@GetMapping("/signup")
	public String signupPage() {
		return "signup";
	}
	
	@GetMapping("/home")
	public String homePage(HttpServletRequest request, Model model) {
		String loggedInUser = CookieManager.getCookie(request, COOKIE_NAME);
		if(loggedInUser.equals("")) return "redirect:/";
		List<Note> allNotes = this.noteService.getAllNotes();
		List<Note> myNotes = this.noteService.getNotesByAuthor(loggedInUser);
		model.addAttribute("allNotes", allNotes);
		model.addAttribute("myNotes", myNotes);
		return "homepage";
	}
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute User user, HttpServletResponse response) {
		System.out.println(user.getUserID() + " " + user.getPassword());
		User existingUser = this.userService.getUser(user.getUserID());
		if(existingUser == null) {
			/* LOG MESSAGE */
			System.out.print("userid invalid");
		} else if(existingUser.getPassword().equals(user.getPassword())) {
			Cookie mCookie = CookieManager.setCookie(COOKIE_NAME, existingUser.getUsername());
			response.addCookie(mCookie);
			return "redirect:/home";
		}
		return "redirect:/";
	}
	
	
	@PostMapping("/register")
	public String signup(@ModelAttribute User user) {
		User exists = this.userService.getUser(user.getUserID());
		if(exists == null) {
			this.userService.saveUser(user);
			return "redirect:/";
		}
		return "redirect:/signup";
		
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file, @RequestParam("subject") String subject, HttpServletRequest request) {
		if(file.isEmpty()) return "redirect:/home";
		
		// create note data
		
		Note note = new Note();
		note.setFileName(file.getOriginalFilename());
		note.setSubject(subject);
		note.setAuthor(CookieManager.getCookie(request, COOKIE_NAME));
		LocalDate currDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedDateString = currDate.format(formatter);
		note.setUploadDate(formattedDateString);
		
		// save to database only if the file is copied successfully
		
		try {
			
            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            this.noteService.saveNote(note);
            
            /* LOG MESSAGE */
            System.out.println("Note Uploaded");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		return "redirect:/home";
	}
	
	@GetMapping("/error")
	public String errorPage() {
		return "error";
	}
}

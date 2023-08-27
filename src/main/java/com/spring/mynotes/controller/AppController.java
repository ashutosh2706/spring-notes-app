package com.spring.mynotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.mynotes.model.User;
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

	private static final String COOKIE_NAME = "loggedInUser";
	
	@GetMapping("/")
	public String welcomePage(HttpServletRequest request) {
		
		// handle the cookie session for already logged in user
		String cookieVal = CookieManager.getCookie(request, COOKIE_NAME);
		if(!cookieVal.equals("")) return "redirect:/home";
		return "login";
	}
	
	
	@GetMapping("/signup")
	public String signupPage(Model model) {
		return "signup";
	}
	
	@GetMapping("/home")
	public String homePage(HttpServletRequest request) {
		
		String loggedInUser = CookieManager.getCookie(request, COOKIE_NAME);
		if(loggedInUser.equals("")) return "redirect:/";
		return "homepage";
	}
	
	
	@PostMapping("/login")
	public String login(@ModelAttribute User user, HttpServletResponse response) {
		System.out.println(user.getUserID() + " " + user.getPassword());
		User existingUser = this.userService.getUser(user.getUserID());
		if(existingUser == null) {
			System.out.print("userid invalid");
		} else if(existingUser.getPassword().equals(user.getPassword())) {
			Cookie mCookie = CookieManager.setCookie(COOKIE_NAME, user.getUsername());
			response.addCookie(mCookie);
			return "redirect:/home";
		}
		return "redirect:/";
	}
	
	
	@PostMapping("/register")
	public String signup(@ModelAttribute User user) {
		this.userService.saveUser(user);
		return "redirect:/";
	}
	
	
	@GetMapping("/error")
	public String errorPage() {
		return "error";
	}
}

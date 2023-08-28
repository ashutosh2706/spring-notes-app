package com.spring.mynotes.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieManager {
	
	public static Cookie setCookie(String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(500000);
		cookie.setPath("/");
		return cookie;
	}
	
	public static String getCookie(HttpServletRequest req, String cookieName) {
		String cookieVal = "";
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) 
                	return cookie.getValue();
            }
        }
		return cookieVal;
	}
}

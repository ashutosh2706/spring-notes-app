package com.spring.mynotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mynotes.dao.UserDao;
import com.spring.mynotes.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	public User saveUser(User user) {
		return userDao.save(user);	
	}
	
	public User getUser(String userID) {
		return userDao.getUserByUserID(userID);
	}
}

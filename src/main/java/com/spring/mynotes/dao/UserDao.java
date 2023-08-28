package com.spring.mynotes.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.mynotes.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
	// custom methods
	public User getUserByUserID(String userID);

}

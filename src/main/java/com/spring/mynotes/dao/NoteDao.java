package com.spring.mynotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.mynotes.model.Note;

@Repository
public interface NoteDao extends JpaRepository<Note, Long> {
	// custom methods
	public List<Note> getNoteByAuthor(String author);
	public void deleteAllByFileName(String fileName);
}

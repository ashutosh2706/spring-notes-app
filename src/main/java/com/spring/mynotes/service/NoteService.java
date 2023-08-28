package com.spring.mynotes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.mynotes.dao.NoteDao;
import com.spring.mynotes.model.Note;

@Service
public class NoteService {

	@Autowired
	private NoteDao noteDao;
	
	public Note saveNote(Note note) {
		return noteDao.save(note);
	}
	
	public List<Note> getAllNotes() {
		return noteDao.findAll();
	}
	
	public List<Note> getNotesByAuthor(String author) {
		return noteDao.getNoteByAuthor(author);
	}
}

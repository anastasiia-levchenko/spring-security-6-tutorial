package com.levchenko.security.controller;

import com.levchenko.security.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class StudentController
{
	private List<Student> students = new ArrayList<>(List.of(
			new Student(1, "Adam", 60),
			new Student(2, "John", 65),
			new Student(3, "Sarah", 70)
	));

	@GetMapping("/students")
	public List<Student> getStudents()
	{
		return students;
	}

	@PostMapping("/students")
	public Student addStudent(@RequestBody final Student student)
	{
		students.add(student);
		return student;
	}

	@GetMapping("/csfr-token")
	public CsrfToken getCsfr(final HttpServletRequest request)
	{
		return (CsrfToken) request.getAttribute("_csrf");
	}
}

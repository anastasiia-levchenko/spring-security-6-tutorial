package com.levchenko.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController
{
	@GetMapping("/")
	public String hello(final HttpServletRequest request)
	{
		return "Hello Nastia. Session id: " + request.getSession().getId();
	}
}

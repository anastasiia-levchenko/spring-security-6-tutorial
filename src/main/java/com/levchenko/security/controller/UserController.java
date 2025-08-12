package com.levchenko.security.controller;

import com.levchenko.security.model.User;
import com.levchenko.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController
{
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public User register(@RequestBody final User user)
	{
		return userService.register(user);
	}

	@PostMapping("/login")
	public String login(@RequestBody User user)
	{
		return userService.verify(user);
	}
}


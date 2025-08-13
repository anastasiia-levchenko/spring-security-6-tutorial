package com.levchenko.security.service;

import com.levchenko.security.dao.UserRepository;
import com.levchenko.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService
{
	@Autowired
	private UserRepository repository;
	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private JWTService jwtService;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // strength = rounds

	public User register(final User user)
	{
		user.setPassword(encoder.encode(user.getPassword()));
		return repository.save(user);
	}

	// checking if the user logging in is valid
	public String verify(final User user)
	{
		final Authentication auth = manager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if (auth.isAuthenticated())
		{
			return jwtService.generateToken(user.getUsername());
		}
		else
		{
			return "fail";
		}
	}
}

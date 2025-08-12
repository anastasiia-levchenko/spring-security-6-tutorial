package com.levchenko.security.securityconfig;

import com.levchenko.security.dao.UserRepository;
import com.levchenko.security.model.User;
import com.levchenko.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

// what user is able to receive access to resource
@Service
public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		final User user = repository.findByUsername(username);

		if(Objects.isNull(user)){
			System.out.println("user not found");
			throw new UsernameNotFoundException("user not found");
		}
		return new UserPrincipal(user);
	}
}

package com.levchenko.security.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity // do not use default security, use custom config instead
public class SecurityConfig
{
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception
	{
		http.csrf(customizer -> customizer.disable());
		http.authorizeHttpRequests(request -> request.requestMatchers("register", "login").permitAll() // no auth required for paths specified
				.anyRequest().authenticated()); // ask all *other* requests for authorization.
		// http.formLogin(Customizer.withDefaults());// Security default Login form (for browser), but with STATELESS it will keep returning to the login page after login
		http.httpBasic(Customizer.withDefaults()); // for Postman Rest API requests
		http.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));// everytime new session
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // instead of using UN/PW filter, we use JWT filter first

		return http.build();

	}


/*		Customizer<CsrfConfigurer<HttpSecurity>> customCsrf = new Customizer<CsrfConfigurer<HttpSecurity>>()
		{
			@Override
			public void customize(final CsrfConfigurer<HttpSecurity> customizer)
			{
				customizer.disable();
			}
		};*/

	/*
	Lets Spring Security know how to authorize the user
	not a good idea because the users are hardcoded
	 */
	/*@Bean
	public UserDetailsService userDetailsService()
	{
		final UserDetails user1 = User
				.withDefaultPasswordEncoder()
				.username("min")
				.password("nim")
				.roles("USER")
				.build();
		final UserDetails user2 = User
				.withDefaultPasswordEncoder()
				.username("max")
				.password("xam")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user1, user2); // both of the users will have access
	}*/


	/*
	Lets Spring Security know how to authorize the user
	 */
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // there are different auth providers, this one responsible for getting data from B
		authenticationProvider.setPasswordEncoder(
				new BCryptPasswordEncoder(12)); // to work properly, password encoder needs to be passed
		authenticationProvider.setUserDetailsService(userDetailsService); // how to get the user of proper authority
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception
	{
		return configuration.getAuthenticationManager();
	}
}

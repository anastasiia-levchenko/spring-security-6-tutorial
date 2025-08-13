package com.levchenko.security.securityconfig;

import com.levchenko.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Component
public class JwtFilter extends OncePerRequestFilter
{
	@Autowired
	private JWTService jwtService;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain)
			throws ServletException, IOException // We get Bearer Token from request headers
	{
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer "))
		{
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}

		// no need to proceed if the user already authenticated
		if (Objects.nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			final UserDetails userDetails = applicationContext.getBean(CustomUserDetailsService.class).loadUserByUsername(username);
			if (jwtService.validateToken(token, userDetails))
			{
				final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}

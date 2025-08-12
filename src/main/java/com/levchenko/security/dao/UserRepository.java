package com.levchenko.security.dao;

import com.levchenko.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByUsername(final String username);
}

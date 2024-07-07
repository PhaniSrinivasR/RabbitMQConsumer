package com.rabbitMQ.RabbitMQConsumer.service;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rabbitMQ.RabbitMQConsumer.model.User;
import com.rabbitMQ.RabbitMQConsumer.repository.UserRepository;
import com.rabbitMQ.RabbitMQProducer.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public String registerUser(User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		repository.save(user);
		return jwtService.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), new ArrayList<>()));
	}

	public String authenticateUser(LoginRequest loginRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect email or password", e);
		}
		final UserDetails userDetails = loadUserByUsername(loginRequest.getEmail());
		return jwtService.generateToken(userDetails);
	}

	private UserDetails loadUserByUsername(String email) {
		return repository.findByEmail(email)
				.map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
						new ArrayList<>()))
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

}

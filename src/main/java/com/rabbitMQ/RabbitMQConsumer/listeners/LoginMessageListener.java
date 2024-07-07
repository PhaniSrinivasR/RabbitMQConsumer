package com.rabbitMQ.RabbitMQConsumer.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitMQ.RabbitMQConsumer.service.AuthenticationService;
import com.rabbitMQ.RabbitMQProducer.dto.LoginRequest;

@Component
public class LoginMessageListener {
    @Autowired
	private AuthenticationService service;

	@RabbitListener(queues = "${rabbitmq.queue.login.name}")
    public void receiveLoginMessage(LoginRequest loginRequest) {
        try {
			String jwtToken = service.authenticateUser(loginRequest);
            System.out.println("User logged in with JWT token: " + jwtToken);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }
}

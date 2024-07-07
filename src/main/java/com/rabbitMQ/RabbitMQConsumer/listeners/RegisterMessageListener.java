package com.rabbitMQ.RabbitMQConsumer.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitMQ.RabbitMQConsumer.model.User;
import com.rabbitMQ.RabbitMQConsumer.service.AuthenticationService;
import com.rabbitMQ.RabbitMQProducer.dto.RegisterRequest;

@Component
public class RegisterMessageListener {
    @Autowired
	private AuthenticationService service;

	@RabbitListener(queues = "${rabbitmq.queue.register.name}")
    public void receiveRegisterMessage(RegisterRequest registerRequest) {
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());

		String jwtToken = service.registerUser(user);
        System.out.println("User registered with JWT token: " + jwtToken);
    }
}

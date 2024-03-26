package com.swprojects.tutorial_web_services.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swprojects.tutorial_web_services.entities.Order;
import com.swprojects.tutorial_web_services.repositories.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	public List<Order> findAll(){
		return orderRepository.findAll();
	}
	
	public Order findById(Long id) {
		@SuppressWarnings("null")
		Optional<Order> obj = orderRepository.findById(id);
		return obj.get();
	}
}
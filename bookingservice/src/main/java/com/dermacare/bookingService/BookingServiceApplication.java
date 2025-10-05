package com.dermacare.bookingService;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dermacare.bookingService.dto.RelationInfoDTO;
import com.dermacare.bookingService.entity.Booking;
import com.dermacare.bookingService.repository.BookingServiceRepository;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BookingServiceApplication {
	
	@Autowired
	private BookingServiceRepository repository;
	
	public void relation() {
	try {
		String customerId = "cr21";
		List<Booking> bookings = repository.findByCustomerId(customerId);
		 Map<Object,Object> data = bookings.stream().
		map(n->{
			Map<String,RelationInfoDTO> relations = new LinkedHashMap<>();
			RelationInfoDTO dto = new RelationInfoDTO();
			dto.setAddress(n.getPatientAddress());
			dto.setAge(n.getAge());
			dto.setFullname(n.getName());
			dto.setMobileNumber(n.getMobileNumber());
			dto.setRelation(n.getRelation());
			Set<RelationInfoDTO> st = new LinkedHashSet<>();
			st.add(dto);
			relations.put(n.getRelation(),dto);
			return relations;
		}).collect(Collectors.toMap(n->n.keySet(),n->n.values()));
		System.out.println(data);
	}catch(Exception e) {
		
	}}
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
		BookingServiceApplication b = new BookingServiceApplication();
		b.relation();
	}

}

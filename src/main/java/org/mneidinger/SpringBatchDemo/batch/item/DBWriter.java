package org.mneidinger.SpringBatchDemo.batch.item;

import java.util.List;

import org.mneidinger.SpringBatchDemo.jpa.model.User;
import org.mneidinger.SpringBatchDemo.jpa.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBWriter implements ItemWriter<User>{

	@Autowired
	private UserRepository userRepository; 
	
	@Override
	public void write(List<? extends User> users) throws Exception {
		
		System.out.println("Data Saved for users: " + users);
		userRepository.saveAll(users);
		
	}

}

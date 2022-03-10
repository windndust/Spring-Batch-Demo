package org.mneidinger.SpringBatchDemo.jpa.repository;

import org.mneidinger.SpringBatchDemo.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>  {

	
	
}

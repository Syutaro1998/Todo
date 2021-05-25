package com.example.demo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TasksRepository extends JpaRepository<Tasks,Integer> {
	
	List<Tasks> findByTaskIdOrderByTaskCodeAsc(int userId);

	void deleteByTaskCode(int taskCode);

	List<Tasks> findByTaskIdOrderByTaskDeadline(int userId);

}

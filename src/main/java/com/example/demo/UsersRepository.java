package com.example.demo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

 Optional<Users> findByUserNameAndUserPass(String name, String pass);
 Users findByUserName(String name);

}

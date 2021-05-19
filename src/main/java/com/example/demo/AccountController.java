package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AccountController {
	
	@Autowired
	UsersRepository userRepository;
	
	@RequestMapping("/")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView login(
		@RequestParam("name") String name,
		@RequestParam("pass") String pass,
			ModelAndView mv
			) {
		
		Optional<Users> user = userRepository.findByUserNameAndUserPass(name,pass);
		
		if(user.isPresent()) {
			mv.setViewName("task");
		}else {
			mv.addObject("message","ID,パスワードが間違ってるよ");
			mv.setViewName("login");
		}
		return mv;
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "login";
	}
	

}

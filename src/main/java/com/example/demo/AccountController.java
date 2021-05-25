package com.example.demo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AccountController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	TasksRepository tasksRepository;
	
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
		
		Optional<Users> user = usersRepository.findByUserNameAndUserPass(name,pass);
		
		if(user.isPresent()) {
			
			Users user_findId = usersRepository.findByUserName(name);
			
			int userId = user_findId.getUserId();
			List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
			
			LocalDateTime dateTo = LocalDateTime.now();

			 for(Tasks tasks : taskList) {
		        	Timestamp time1 = (Timestamp) tasks.getTaskDeadline();
		        	LocalDateTime dateFrom = time1.toLocalDateTime();
		        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
		        	tasks.setLimited(limited);
		        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
		        }

			mv.addObject( "taskList",taskList);
			session.setAttribute("name", name);
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
	
	@RequestMapping("/newbee")
	public String newbee() {

		return "newbee";
	}
	
	@RequestMapping(value="/newbee",method=RequestMethod.POST)
	public ModelAndView newbee(
			@RequestParam("name") String name,
			@RequestParam("pass") String pass,
			ModelAndView mv) {
		
		String message1 = "";
		String message2 = "";
		
		if(name.isEmpty()) {
			message1 = "名前が未入力です。";
		}else {
			List<Users> users = usersRepository.findAll();
			for(Users user : users) {
				if(user.getUserName().equals(name))
					message1 = "そのアカウント名は既に使われています。";
			}
		}
		if(pass.isEmpty()) {
			message2 = "パスワードが未入力です。";
		}else {
			
		}
		
		if(!message1.isEmpty() ||
				!message2.isEmpty()) {
			mv.addObject("message1", message1);
			mv.addObject("message2", message2);
			mv.setViewName("newbee");
			return mv;
		}else {
			Users user = new Users(name,pass);
			usersRepository.saveAndFlush(user);
			session.setAttribute("name", name);
			
			mv.setViewName("task");
		}
		
		return mv;
	}
	

}

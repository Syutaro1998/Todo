package com.example.demo;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	TasksRepository tasksRepository; 
	
	@Autowired
	UsersRepository usersRepository; 
	

	@RequestMapping("/task")
	public   ModelAndView task(ModelAndView mv) {
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
		
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		mv.addObject(taskList);
		return mv;
	}
	
	
	@RequestMapping("/add")
	public ModelAndView add(ModelAndView mv) {
		Date day = new Date();
		 SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		 String display = d1.format(day);
		 
		 mv.addObject("hi",display);
		 System.out.println(display);
		 mv.setViewName("add");
		return mv;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public ModelAndView task_add(
			@RequestParam("calendar") String calendar,
			@RequestParam("title") String title,
			@RequestParam("task_add") String add,
			ModelAndView mv
			) {
		boolean flg = false;
		
		try {
			SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
			Date a = d1.parse(calendar);
			
			
			String name = (String) session.getAttribute("name");
			Users user = usersRepository.findByUserName(name);
			
			int userId = user.getUserId();
			Tasks tasks = new Tasks(add,a,flg,title,userId);
			tasksRepository.saveAndFlush(tasks);
			List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
			mv.addObject(taskList);
			
			mv.setViewName("redirect:/task");
		}catch(Exception e){
			e.printStackTrace();
			mv.setViewName("/login");
		}
		
		return mv;
	}
	
	@RequestMapping("/retrun")
	public ModelAndView return1(
			ModelAndView mv
			) {
		
		List<Tasks> taskList =  tasksRepository.findAll();
		mv.addObject(taskList);
		mv.setViewName("task");
		
		return mv;
	}
	
	@RequestMapping("/edit/{taskCode}")
	public ModelAndView edit(
			@PathVariable(name="taskCode") int taskCode,
			ModelAndView mv) {
		Date day = new Date();
		 SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		 String display = d1.format(day);
		 Optional<Tasks> taskList = tasksRepository.findById(taskCode);
		 
		 
		 if(taskList.isPresent()) {
			 session.setAttribute("task", taskList.get());
		 }
		 mv.addObject("hi",display);
		 mv.setViewName("edit");
		return mv;
	}
	
	
	@RequestMapping(value="/task_edit",method=RequestMethod.POST)
	public ModelAndView edit(
			@RequestParam("calendar") String calendar,
			@RequestParam("job") String edit,
			@RequestParam("title") String title,
			@RequestParam("pricode") String pricode,
			ModelAndView mv
			) {
		boolean flg = false;
		
		try {
			int priocode = Integer.parseInt(pricode);
		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
		Date a = d1.parse(calendar);
		Tasks tasks = new Tasks(priocode,title,edit,a,flg);
		tasksRepository.saveAndFlush(tasks);
		List<Tasks> taskList =  tasksRepository.findAll();
		mv.addObject(taskList);
		
		for(Tasks task : taskList) {
		System.out.println(task.getTaskDeadline());
		}
		mv.setViewName("task");
		}catch(Exception e){
			e.printStackTrace();
			mv.setViewName("/login");
		}
		
		return mv;
	}
	
	@RequestMapping("/sort_down")
	public ModelAndView down(
			ModelAndView mv
			) {
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
		
		int userId = user.getUserId();
		
		List<Tasks> taskList = tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		mv.addObject(taskList);
		mv.setViewName("task");
		
		return mv;
	}
}

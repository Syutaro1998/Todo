package com.example.demo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
	
	boolean flg = false;
	
	int[] num = {1,2,3,4,5,6,7,8,9,10};
	
	

	@RequestMapping("/task")
	public   ModelAndView task(ModelAndView mv) {
		
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
		
		session.setAttribute("num", num[0]);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();
		
		 for(Tasks task : taskList) {
	        	Timestamp time1 = (Timestamp) task.getTaskDeadline();
	        	LocalDateTime dateFrom = time1.toLocalDateTime();
	        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
	        	task.setLimited(limited);
	        }
		mv.addObject("taskList",taskList);
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
			
			calendar = calendar.toString() + " 00:00:00";
			Timestamp day = Timestamp.valueOf(calendar);

			String name = (String) session.getAttribute("name");
			Users user = usersRepository.findByUserName(name);
			
			int userId = user.getUserId();
			Tasks tasks = new Tasks(add , day , flg , title , userId);
			tasksRepository.saveAndFlush(tasks);
			List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
			
			LocalDateTime dateTo = LocalDateTime.now();

			 for(Tasks task : taskList) {
		        	Timestamp time1 = (Timestamp) task.getTaskDeadline();
		        	LocalDateTime dateFrom = time1.toLocalDateTime();
		        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
		        	task.setLimited(limited);
		        }
			mv.addObject("taskList",taskList);
			
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
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();

		for(Tasks tasks : taskList) {
        	Timestamp time1 = (Timestamp) tasks.getTaskDeadline();
        	LocalDateTime dateFrom = time1.toLocalDateTime();
        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
        	tasks.setLimited(limited);
        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
        }
		 
		mv.addObject("taskList",taskList);
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
			@RequestParam("taskCode") int taskCode,
			ModelAndView mv
			) {
		
		try {
			calendar = calendar.toString() + " 00:00:00";
			Timestamp day = Timestamp.valueOf(calendar);
			Optional<Tasks> task =  tasksRepository.findById(taskCode);
			
			if(task.isPresent()) {
				task.get().setTaskContent(edit);
				task.get().setTaskTitle(title);
				task.get().setTaskDeadline(day);
				
				tasksRepository.saveAndFlush(task.get());
			}
			String name = (String) session.getAttribute("name");
			Users user = usersRepository.findByUserName(name);
			
			int userId = user.getUserId();
			List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
			
			LocalDateTime dateTo = LocalDateTime.now();
			
			 for(Tasks taskex: taskList) {
		        	Timestamp time1 = (Timestamp) taskex.getTaskDeadline();
		        	LocalDateTime dateFrom = time1.toLocalDateTime();
		        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
		        	taskex.setLimited(limited);
		        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
		        }
			mv.addObject("taskList",taskList);
			
			mv.setViewName("redirect:/task");
			}catch(Exception e){
				e.printStackTrace();
				mv.setViewName("/login");
			}
		
			return mv;
		}
	
	@RequestMapping("/comp/{taskCode}")
	public ModelAndView comp(
			@PathVariable(name="taskCode") int taskCode,
			ModelAndView mv) {		
		Optional<Tasks> task1 =  tasksRepository.findById(taskCode);
		
		if(task1.isPresent()) {
			if(task1.get().isTaskFlg()) {
				task1.get().setTaskFlg(false);
				tasksRepository.saveAndFlush(task1.get());
			}else {
				task1.get().setTaskFlg(true);
				tasksRepository.saveAndFlush(task1.get());
			}
		}
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
		
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();
		
		 for(Tasks task: taskList) {
	        	Timestamp time1 = (Timestamp) task.getTaskDeadline();
	        	LocalDateTime dateFrom = time1.toLocalDateTime();
	        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
	        	task.setLimited(limited);
	        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
	        }
		mv.addObject("taskList",taskList);
		 
		 mv.setViewName("redirect:/task");
		return mv;
	}
	
	
	@RequestMapping("/sort_down")
	public ModelAndView down(
			ModelAndView mv
			) {
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();
		
		 for(Tasks task : taskList) {
	        	Timestamp time1 = (Timestamp) task.getTaskDeadline();
	        	LocalDateTime dateFrom = time1.toLocalDateTime();
	        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
	        	task.setLimited(limited);
	        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
	        }
		mv.addObject("taskList",taskList);
		
		mv.setViewName("task");
		
		return mv;
	}
	
	@RequestMapping("/delete")
	public ModelAndView delete(
			@RequestParam(name="delete") int taskCode,
			ModelAndView mv) {
		
		tasksRepository.deleteById(taskCode);
		
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();
		
		 for(Tasks tasks : taskList) {
	        	Timestamp time1 = (Timestamp) tasks.getTaskDeadline();
	        	LocalDateTime dateFrom = time1.toLocalDateTime();
	        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
	        	tasks.setLimited(limited);
	        }
		mv.addObject("taskList",taskList);
		
		 mv.setViewName("redirect:/task");
		return mv;
	}
	
	@RequestMapping("/sort_lim")
	public ModelAndView sort_lim(
			ModelAndView mv
			) {
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskDeadline(userId);
		
		LocalDateTime dateTo = LocalDateTime.now();
		
		 for(Tasks task : taskList) {
	        	Timestamp time1 = (Timestamp) task.getTaskDeadline();
	        	LocalDateTime dateFrom = time1.toLocalDateTime();
	        	int limited = (int) ChronoUnit.DAYS.between(dateTo,dateFrom);
	        	task.setLimited(limited);
	        	//System.out.println(ChronoUnit.DAYS.between(dateTo,dateFrom));
	        }
		mv.addObject("taskList",taskList);
		
		mv.setViewName("task");
		
		return mv;
	}
	
	@RequestMapping("/fin")
	public ModelAndView fin(
			ModelAndView mv
			) {
		String name = (String) session.getAttribute("name");
		Users user = usersRepository.findByUserName(name);
				
		int userId = user.getUserId();
		List<Tasks> taskList =  tasksRepository.findByTaskIdOrderByTaskCodeAsc(userId);
		mv.addObject("taskList",taskList);
		mv.setViewName("fin");
		return mv;
	}
	
	
}

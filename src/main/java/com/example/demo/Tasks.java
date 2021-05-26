package com.example.demo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tasks")
public class Tasks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="task_code")
	private Integer taskCode;
	
	@Column(name="task_id")
	private Integer taskId;
	
	@Column(name="task_title")
	private String taskTitle;
	
	@Column(name="task_content")
	private String taskContent;
	
	@Column(name="task_deadline")
	private Date taskDeadline;
	
	@Column(name="task_flg")
	private boolean taskFlg;
	
	@Transient
	private long limited;

	public Tasks() {
	}
	

	public Tasks(Integer taskCode, boolean taskFlg) {
		this.taskCode = taskCode;
		this.taskFlg = taskFlg;
	}


	public Tasks(Integer taskCode, String taskTitle, String taskContent, Date taskDeadline, boolean taskFlg) {
		this.taskCode = taskCode;
		this.taskTitle = taskTitle;
		this.taskContent = taskContent;
		this.taskDeadline = taskDeadline;
		this.taskFlg = taskFlg;
	}

	public Tasks(String taskContent, Date taskDeadline, boolean taskFlg,String taskTitle,int taskId) {
		this.taskContent = taskContent;
		this.taskDeadline = taskDeadline;
		this.taskFlg = taskFlg;
		this.taskTitle = taskTitle;
		this.taskId = taskId;
	}

	public Tasks(Integer taskCode, Integer taskId, String taskTitle, String taskContent, Date taskDeadline,
			boolean taskFlg) {
		this.taskCode = taskCode;
		this.taskId = taskId;
		this.taskTitle = taskTitle;
		this.taskContent = taskContent;
		this.taskDeadline = taskDeadline;
		this.taskFlg = taskFlg;
	}


	public Integer getTaskCode() {
		return taskCode;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public Date getTaskDeadline() {
		return taskDeadline;
	}

	public boolean isTaskFlg() {
		return taskFlg;
	}
	
	public void setLimited(long limited) {
		this.limited = limited;
	}

	public long getLimited() {
		return limited;
	}


	public void setTaskFlg(boolean taskFlg) {
		this.taskFlg = taskFlg;
	}


	public void setTaskCode(Integer taskCode) {
		this.taskCode = taskCode;
	}


	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}


	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}


	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}


	public void setTaskDeadline(Date taskDeadline) {
		this.taskDeadline = taskDeadline;
	}


}

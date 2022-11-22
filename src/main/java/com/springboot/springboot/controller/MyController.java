package com.springboot.springboot.controller;

import java.util.List;

import com.springboot.springboot.entities.FormData;
import com.springboot.springboot.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.springboot.springboot.entities.Course;
import com.springboot.springboot.services.CourseService;

@RestController
public class MyController {
	
	@Autowired
	private CourseService courseService;
	@Autowired
	private FormService formService;
//	@GetMapping("/home")
//	public String home() {
//		 
//		
//		return "this is home page";
//	}
//	
	//get the courses
	@GetMapping("/courses")
	public List<Course> getCourses()
	{
	 return	this.courseService.getCourses();
	}
	
	@GetMapping("/courses/{courseId}")
	public Course getCourses(@PathVariable String courseId) {
		return this.courseService.getCourses(Long.parseLong(courseId));
	}
	
	@PostMapping("/courses")
	public ResponseEntity<HttpStatus> addCourse(@RequestBody Course course){
	try{
		 this.courseService.addCourse(course);
		 return new ResponseEntity<>(HttpStatus.OK);
	}catch(Exception e){
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	@PutMapping("/courses")
	public Course updateCourses(@RequestBody Course course) {
		return this.courseService.updateCourse(course);
	}
	@DeleteMapping("/courses/{courseId}")
	public void deleteCourse(@PathVariable String courseId)
	{
		 this.courseService.deleteCourse(Long.parseLong(courseId));
	}
	@PostMapping(value = "/form", consumes = {"*/*"})
	public ResponseEntity addData(@RequestParam String name, String email, String message) {
		 this.formService.addData(name, email, message);
		return ResponseEntity.ok("Your form have been send successfully:\n Thanks For your Interest");
	}
}

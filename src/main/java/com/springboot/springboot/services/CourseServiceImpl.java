package com.springboot.springboot.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.springboot.dao.CourseDao;
import com.springboot.springboot.entities.Course;
@Service
public class CourseServiceImpl implements CourseService {
	@Autowired
	private CourseDao courseDao;
	
//	List<Course> list;
	
	public CourseServiceImpl() {
//		list= new ArrayList<>();
//		list.add(new Course(145,"java core course","this course for java core concepts"));
//		list.add(new Course(4343,"Spring book course","Creating rest api spring boot course"));
	}

	@Override
	public List<Course> getCourses() {
		
		return courseDao.findAll();
	}

	@Override
	public Course getCourses(long courseId) {
		
//		Course c=null;
//		for(Course course: list) {
//			if (course.getId()==courseId)
//			{
//				c=course;
//						break;
//			}
//		}
		
		return courseDao.getReferenceById(courseId);
	}

	@Override
	public Course addCourse(Course course) {
//		list.add(course);
		courseDao.save(course);
		return course;
	}

	@Override
	public Course updateCourse(Course course) {
		// TODO Auto-generated method stub
		courseDao.save(course);
		return course;
	}

	@Override
	public void deleteCourse(long courseId) {
		// TODO Auto-generated method stub
		Course entity=courseDao.getReferenceById(courseId);
		courseDao.delete(entity);
	}  


}

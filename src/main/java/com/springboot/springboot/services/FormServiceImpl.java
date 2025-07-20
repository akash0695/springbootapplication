package com.springboot.springboot.services;

import com.springboot.springboot.dao.FormDao;
import com.springboot.springboot.entities.FormData;
// Akash Gupta
// Akash Gupta

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FormServiceImpl implements FormService {
    @Autowired
    private FormDao formDao;

	@Override
	public FormData addData(String name, String email, String message) {
		FormData data= new FormData();
		data.setName(name);
		data.setEmail(email);
		data.setMessage(message);
		formDao.save(data);
	return data;
	}

	@Override
	public List<FormData> getAllFormSubmissions() {
		return formDao.findAll();
	}

}

package com.springboot.springboot.services;

import com.fasterxml.jackson.core.JsonGenerator;
import com.springboot.springboot.dao.FormDao;
import com.springboot.springboot.entities.FormData;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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


}

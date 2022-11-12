package com.springboot.springboot.services;

import com.springboot.springboot.dao.FormDao;
import com.springboot.springboot.entities.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormServiceImpl implements FormService {
    @Autowired
    private FormDao formDao;
    @Override
    public FormData addData(FormData data) {
        formDao.save(data);
        return data;
    }
}

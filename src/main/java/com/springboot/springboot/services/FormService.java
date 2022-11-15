package com.springboot.springboot.services;

import com.springboot.springboot.entities.FormData;

public interface FormService {
    public FormData addData(String name, String email, String message);
}

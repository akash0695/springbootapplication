package com.springboot.springboot.services;
// Akash Gupta
// Akash Gupta

import com.springboot.springboot.entities.FormData;
import java.util.List;

public interface FormService {
    public FormData addData(String name, String email, String message);
    public List<FormData> getAllFormSubmissions();
}

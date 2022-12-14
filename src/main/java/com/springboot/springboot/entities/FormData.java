package com.springboot.springboot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// Akash Gupta
// Akash Gupta

import javax.persistence.Entity;
import javax.persistence.Id;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class FormData {
   
  
    private String name;
    @Id
    private String email;
    private String message;

    public FormData() {
        super();
    }

    public FormData( String name, String email, String message) {
       
        this.name = name;
        this.email = email;
        this.message = message;
    }

  

 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FormData{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

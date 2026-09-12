package com.springboot.springboot.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.springboot.springboot.services.JwtUserDetailsService;
import com.springboot.springboot.model.UserDTO;

@Component
@Profile({"local","prod-h2"})
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    public void run(String... args) throws Exception {
        String username = "akash";
        try {
            if (userDetailsService.findByUsername(username) == null) {
                UserDTO user = new UserDTO();
                user.setUsername(username);
                user.setPassword("akash@1250");
                user.setEmail("akash@example.com");
                user.setCompanyName("Admin");
                user.setPhone("");
                userDetailsService.saveAdmin(user);
            }
        } catch (Exception e) {
            // Log to stderr; avoid failing startup for unexpected reasons
            System.err.println("DataInitializer: failed to create default admin: " + e.getMessage());
        }
    }
}

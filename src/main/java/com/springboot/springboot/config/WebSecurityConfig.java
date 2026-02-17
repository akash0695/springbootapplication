package com.springboot.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;
	

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// Public access - no authentication required
				.authorizeRequests()
					.antMatchers("/authenticate", "/register").permitAll()  // Authentication endpoints
					.antMatchers("/check-approval-status").permitAll()  // Check approval status
					.antMatchers("/form").permitAll()  // Contact form submission
					.antMatchers("/", "/index.html").permitAll()  // Main website
					.antMatchers("/test.html").permitAll()  // Test page
					.antMatchers("/login.html", "/register.html").permitAll()  // Login/Register pages
					.antMatchers("/check-status.html").permitAll()  // Check approval status page
					.antMatchers("/test-form.html", "/view-submissions.html").permitAll()  // Test pages
					.antMatchers("/test-contact-form.html").permitAll()  // Test contact form
					.antMatchers("/dev-support.html").permitAll()  // Development support page
					.antMatchers("/project-detail.html").permitAll()  // Project detail page
					.antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()  // Static resources
					.antMatchers("/dashboard.html").permitAll()  // Dashboard page (handles auth in frontend)
					.antMatchers("/query-dashboard.html").permitAll()  // Query management page (handles auth in frontend)
					.antMatchers("/assigned-queries.html").permitAll()  // Assigned queries page (handles auth in frontend)
					.antMatchers("/admin-dashboard.html").permitAll()  // Admin dashboard page (handles auth in frontend)
					.antMatchers("/create-admin.html").permitAll()  // Admin creation page
					.antMatchers("/form-submissions").authenticated()  // API requires authentication
					.antMatchers("/api/queries/submit").permitAll()  // Allow query submission from landing page
					.antMatchers("/api/queries/**").authenticated()  // Query management requires authentication
					.antMatchers("/admin/**").authenticated()  // Admin endpoints require authentication
					.anyRequest().authenticated()  // All other requests need authentication
				.and()
				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

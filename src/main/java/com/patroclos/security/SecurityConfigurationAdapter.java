package com.patroclos.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationAdapter extends WebSecurityConfigurerAdapter implements ApplicationContextAware{


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/webjars/**","/resources/**","/h2-console/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.csrf().disable()
		.logout(l -> l
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/processlogin")
				.permitAll()
				)
		.oauth2Login()
			.loginPage("/processlogin")
			.defaultSuccessUrl("/welcome")
			.permitAll()
		.and()
		.formLogin()
			.loginPage("/processlogin")
			.defaultSuccessUrl("/welcome")
			.permitAll();
		

	}




	@Autowired
	private DataSource dataSource;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);		//initialize withone user ( user:password )
	}





}

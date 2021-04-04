package org.seng302.utilities;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security Configuration Class.
 * Deals with the jsessionid cookie, user authentication, and remember me keys for the frontend use.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * Configuration of web security - sets the rules on what user can go where.
     * @param http security configurer.
     * @throws Exception
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http
                .csrf().disable()
                .httpBasic()

                .and()
                .authorizeRequests()
                .antMatchers("/checksession").authenticated()
                .antMatchers(HttpMethod.POST, "/login", "/users").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/login", "/users").permitAll()
                .antMatchers(HttpMethod.GET, "/users/search*").authenticated()
                .antMatchers(HttpMethod.PUT, "/users/{id}/makeAdmin").hasRole("DGAA")
                .anyRequest().permitAll()

                .and()
                .logout()
                ;

    }

}
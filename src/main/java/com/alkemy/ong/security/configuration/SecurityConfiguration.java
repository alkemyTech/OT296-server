package com.alkemy.ong.security.configuration;

import com.alkemy.ong.security.filters.JwtRequestFilter;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/contact/contacts").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/auth/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/auth/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/organization/public").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/organization/public").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/organization/public").hasAnyRole("ADMIN" , "USER")
                .antMatchers(HttpMethod.DELETE, "/news/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/members").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/members/**").hasRole("ADMIN")
                .antMatchers("/storage/uploadFile").permitAll()
                .antMatchers("/storage/download").permitAll()
                .antMatchers("/news/**").hasRole("ADMIN")
                .antMatchers("/storage/**").hasRole("ADMIN")
                .antMatchers("/categories/**").hasRole("ADMIN")
                .antMatchers("/activities/**").hasRole("ADMIN")
                .antMatchers("/Slides/**").hasRole("ADMIN")
                .antMatchers("/auth/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
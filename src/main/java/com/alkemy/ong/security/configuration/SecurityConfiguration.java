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

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()

                //Auth
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/register").permitAll()
                .antMatchers("/auth/me").permitAll()

                //Storage
                .antMatchers(HttpMethod.GET, "/storage/download").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.POST, "/storage/uploadFile").hasRole(ROLE_ADMIN)

                //Auth_Users
                .antMatchers(HttpMethod.GET, "/auth/users").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.GET, "/auth/users/{id}").hasAnyRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/auth/users/{id}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PATCH, "/auth/users/{id}").hasRole(ROLE_ADMIN)

                //News
                .antMatchers(HttpMethod.GET, "/news").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.GET, "/news/{id}").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.PUT, "/news/{id}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/news/{id}").hasRole(ROLE_ADMIN)

                //Categories
                .antMatchers(HttpMethod.GET, "/categories/{id}").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.GET, "/categories").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.POST, "/categories").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/categories/{id}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/categories/{id}").hasRole(ROLE_ADMIN)

                //Testimonials
                .antMatchers(HttpMethod.POST,"/testimonials").hasRole(ROLE_ADMIN)

                //Contacts
                .antMatchers(HttpMethod.GET, "/contact/contacts").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/contact").hasAnyRole(ROLE_ADMIN, ROLE_USER)

                //Activities
                .antMatchers(HttpMethod.POST, "/activities").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/activities/{id}").hasRole(ROLE_ADMIN)

                //Slides
                .antMatchers(HttpMethod.GET, "/Slides/**").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.GET, "/Slides/{id}").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.POST, "/Slides").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/Slides/{id}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/Slides/{id}").hasRole(ROLE_ADMIN)

                //Organization
                .antMatchers(HttpMethod.GET, "/organization/public").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.POST, "/organization/public").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/organization/public").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/organization/public/{id}").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PATCH, "/organization/{id}").hasRole(ROLE_ADMIN)

                //Members
                .antMatchers(HttpMethod.GET, "/members").hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .antMatchers(HttpMethod.POST, "/members").hasAnyRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/members/{id}").hasAnyRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/members/{id}").hasRole(ROLE_ADMIN)

                //Comments
                .antMatchers(HttpMethod.GET, "/comment").hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, "/comment").hasRole(ROLE_USER)
                .antMatchers(HttpMethod.DELETE, "/comment/**").hasAnyRole(ROLE_ADMIN,ROLE_USER)
                .antMatchers(HttpMethod.GET, "/comment/posts/comments").hasAnyRole(ROLE_USER, ROLE_ADMIN)

                //Docs
                .antMatchers("/api/docs/**").permitAll()

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
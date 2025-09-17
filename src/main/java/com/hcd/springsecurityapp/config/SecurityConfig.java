package com.hcd.springsecurityapp.config;

import com.hcd.springsecurityapp.listener.CustomHttpSessionListener;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.ServletContextAware;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements ServletContextAware {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer ->
                        authorizeHttpRequestsCustomizer.requestMatchers("/").permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLoginCustomizer ->
                        formLoginCustomizer.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/home"))
                .logout(logoutCustomizer ->
                        logoutCustomizer.invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutSuccessUrl("/"))
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.maximumSessions(1)
                                .expiredUrl("/home"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("horatiucd")
                .password(passwordEncoder.encode("a"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.addListener(new CustomHttpSessionListener());
    }
}

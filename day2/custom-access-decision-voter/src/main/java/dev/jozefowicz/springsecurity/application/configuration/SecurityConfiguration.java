package dev.jozefowicz.springsecurity.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            // TODO skonfiguruj własnego AccessDecisionManagera
            .and()
            .httpBasic();
    }

    // TODO zdefiniuj bean z AccessDecisionManagerem, który ma zdefiniowane dwa AccessDecisionVotery:

    // * WebExpressionVoter - używamy .anyRequest().authenticated() co w praktyce buduje wyrażenie access("isAuthenticated()")
    // * RoleVoter
    // * MiddayAccessDecisionVoter

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (username.equals("jan@example.com")) {
                return new User("jan@example.com", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
            } else if (username.equals("stefan@example.com")) {
                return new User("stefan@example.com", "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            }
            throw new UsernameNotFoundException("");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /*
        TODO przetestuj po zaimplementowaniu, można manipulować datą aby ułatwić sprawdzanie warunków
        Użyte jest uwierzytelnianie HTTP Basic
     */

}

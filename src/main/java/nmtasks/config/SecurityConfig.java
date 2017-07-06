package nmtasks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import nmtasks.services.AuthService;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService authService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().permitAll()
                .antMatchers("/h2").permitAll()
                .antMatchers("/tasks","/api/**").authenticated()
                .and()
            .formLogin()
                .loginPage("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
            .logout()
                .logoutUrl("/?logout")
                .and()
            .httpBasic().and()
            .exceptionHandling()
                .accessDeniedPage("/?unauthorized")
                .and()
            .headers().frameOptions().disable().and()
            .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .userDetailsService(authService)
        .passwordEncoder(new BCryptPasswordEncoder());
    }
}
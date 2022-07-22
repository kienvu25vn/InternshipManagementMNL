package com.quanlisinhvienmnl.security;


import com.quanlisinhvienmnl.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // Các trang không yêu cầu login
        http.authorizeRequests().antMatchers("/", "/login", "/logout" ,"/loginError" , "/logoutSuccessful" , "/home" ).permitAll();


        // Nếu chưa login, nó sẽ redirect tới trang /login.

        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers("/mentor").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/position").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/companycard").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/university").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/internshipdelete").permitAll();


        //Trang dành cho ADMIN , MENTOR
        http.authorizeRequests().antMatchers("/internship").access("hasAnyRole('ROLE_ADMIN' , 'ROLE_MENTOR')");




        // Khi người dùng đã login, với vai trò XX.
        // Nhưng truy cập vào trang yêu cầu vai trò YY,
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Cấu hình cho Login Form.
        http.authorizeRequests().and().formLogin()
                // Submit URL của trang login
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")//
                .defaultSuccessUrl("/home" )//
                .failureUrl("/loginError")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Cấu hình cho Logout Page.
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");

        // Cấu hình Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }
}

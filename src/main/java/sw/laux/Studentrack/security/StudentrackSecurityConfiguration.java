package sw.laux.Studentrack.security;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class StudentrackSecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Qualifier("userService")
    @Autowired
    private UserDetailsService userSecurityService;

    @Autowired
    private StudentrackSecurityUtilities securityUtilities;

    @Autowired
    private Logger logger;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    private static final String[] ALLOW_ACCESS_WITHOUT_AUTHENTICATION = {
            "/css/**", "/image/**", "/fonts/**", "/js/**", "/", "/login", "/registration", "/registration/check", "/impress", "/privacy", "/api/**", "/developer", "/developer/check"
    };

    private static final String[] ALLOW_ACCESS_FOR_LECTURERS = {"/modules/edit/*", "/modules/grade/*"};
    private static final String[] ALLOW_ACCESS_FOR_STUDENTS = {"/timeorders/*", "/change/premium/*"};
    private static final String[] ALLOW_ACCESS_FOR_ALL = {"/home", "/modules", "/modules/new**", "/change", "/change/check"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ALLOW_ACCESS_FOR_STUDENTS).hasAuthority("Student")
                .antMatchers(ALLOW_ACCESS_FOR_LECTURERS).hasAuthority("Lecturer")
                .antMatchers(ALLOW_ACCESS_FOR_ALL).hasAnyAuthority("Student", "Lecturer")
                .antMatchers(ALLOW_ACCESS_WITHOUT_AUTHENTICATION)
                .permitAll().anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    var userDetails = (UserDetails) authentication.getPrincipal();
                    logger.info("User with mail address " + userDetails.getUsername() + " logged in.");
                    httpServletResponse.sendRedirect("home");
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    var username = httpServletRequest.getParameter("username");
                    logger.info("Failed login try for " + username);
                    httpServletResponse.sendRedirect("login-error");
                })
                .failureUrl("/login-error")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout")
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    var userDetails = (UserDetails) authentication.getPrincipal();
                    logger.info("Logout for " + userDetails.getUsername());
                    httpServletResponse.sendRedirect("/?logout");
                })
                .deleteCookies("remember-me", "JSESSIONID")
                .permitAll()
                .and()
                .rememberMe();
        http.csrf().disable();
        http.rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }
}

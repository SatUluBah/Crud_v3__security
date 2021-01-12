package web.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.config.handler.SuccessUserHandler;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .formLogin()
                .loginPage("/login")                // указываем страницу с формой логина
                .successHandler(successUserHandler) //указываем логику обработки при логировании
                .loginProcessingUrl("/login")       // указываем action с формы логина
                .usernameParameter("username")      // Указываем параметры логина и пароля с формы логина
                .passwordParameter("password")
                .permitAll()                        // даем доступ к форме логина всем
                .and().csrf().disable();            //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)

        http
                .logout()
                .permitAll()                                                  // разрешаем делать логаут всем
//              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))   // указываем URL логаута
                .logoutSuccessUrl("/login");                                  // указываем URL при удачном логауте


//        http
//                .authorizeRequests()
//                .antMatchers("/login").anonymous()            //страницы аутентификаци доступна всем
//                .antMatchers("/user/**").hasRole("USER")      // защищенные URL
//                .antMatchers("/**").hasRole("ADMIN")          // защищенные URL
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().accessDeniedPage("/access-denied");
        http
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers("/user/**").hasAnyRole( "USER")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public static NoOpPasswordEncoder passwordEncoder() {
//        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//    }

}

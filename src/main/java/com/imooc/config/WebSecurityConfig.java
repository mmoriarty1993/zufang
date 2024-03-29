package com.imooc.config;

import com.imooc.security.AuthProvider;
import com.imooc.security.LoginAuthFailHandler;
import com.imooc.security.LoginUrlEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Http权限控制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //资源访问权限
        http.authorizeRequests()
                .antMatchers("/admin/login").permitAll()//管理员登录入口
                .antMatchers("/user/login").permitAll()//用户登录入口
                .antMatchers("/static/**").permitAll()//静态资源
                .antMatchers("/admin/**").hasRole("ADMIN")//
                .antMatchers("/user/**").hasAnyRole("ADMIN","USER")//
                .antMatchers("/api/user/**").hasAnyRole("ADMIN","USER")//
                .and()
                .formLogin()
                .loginProcessingUrl("/login")//角色登陆处理入口
                .failureHandler(authFailHandler())//验证失败处理器
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/page")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint())
                .accessDeniedPage("/403").and();

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }
//    /**
//     * 自定义内存认证策略
//     */
//    @Autowired
//    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN").and();
//    }
        @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
        }

    @Bean
    public AuthProvider authProvider(){
        return new AuthProvider();
    }

    @Bean
    public LoginUrlEntryPoint urlEntryPoint(){
        return new LoginUrlEntryPoint("/user/login");
    }

    @Bean
    public LoginAuthFailHandler authFailHandler(){
        return new LoginAuthFailHandler(urlEntryPoint());
    }
}

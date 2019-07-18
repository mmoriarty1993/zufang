package com.imooc.config;

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
                .loginProcessingUrl("/login")//角色登陆入口界面
                .and();
        super.configure(http);
    }
}

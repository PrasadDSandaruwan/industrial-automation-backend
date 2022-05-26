package com.industrialautomation.api.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("restAPI");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/v1/user/add-user","/v1/user/get-all-users" )
                .hasAnyAuthority("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/v1/user/user-details","/v1/user/force-password-change",
                        "/v1/user/change-password","/v1/user/first-time-change-password"
                        )
                .authenticated()


                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/v1/user/change-password","/v1/user/first-time-change-password",
                        "/v1/user/update-profile"
                )
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/v1/alarm/add","/v1/alarm/edit/**","/v1/alarm/delete/**",
                        "/v1/alarm/all","/v1/alarms/details/**")
                .permitAll()



                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE,
                        "/v1/user/delete/**"
                )
                .hasAnyAuthority("ADMIN")


        ;
    }
}

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
                        "/v1/user/add-user","/v1/user/get-all-users" ,"/v1/alarm/add","/v1/alarm/edit/**"
                ,"/v1/production-line/add","/v1/machine-type/add",
                        "/v1/machine/add","/v1/machine/edit/**","/v1/command/add","/v1/command/edit/**",
                        "/v1/connected-machine/add","/v1/connected-machine/edit"
                )
                .hasAnyAuthority("ADMIN")



                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE,"/v1/machine/delete/**","/v1/alarm/delete/**","/v1/user/delete/**")
                .hasAnyAuthority("ADMIN")



                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,  "/v1/production-line/all","/v1/machine-type/all","/v1/connected-machine/possible/**"
                , "/v1/alarms/unique/**","/v1/machine/unique/**","/v1/production-line/unique/**","/v1/user/unique/"

                )
                .hasAnyAuthority("ADMIN")


                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/v1/user/user-details","/v1/user/force-password-change",
                        "/v1/user/change-password","/v1/user/first-time-change-password","/v1/alarm/all","/v1/alarms/details/**",
                        "/v1/machine/all","/v1/machine/details/**","/v1/machine/get-by-line","/v1/machine/id-list"

                )
                .authenticated()


                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/v1/user/change-password","/v1/user/first-time-change-password",
                        "/v1/user/update-profile","/v1/rates/get-rates","/v1/rates/add","/v1/connected-machine/rate"

                )
                .authenticated()
        ;
    }
}

package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.filter.CustomAuthenticationFilter;
import com.ecommerce.ecommerce.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/users/add","/users/refreshToken").permitAll();
        http.authorizeRequests().antMatchers("/users/addToCart","/users/getUserCart","/users/removeFromCart","/users/getMyDetails","/users/modifyMyDetails").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/products/buy","/products/buyMyCart").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/products/getHotProduct").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/products/getProductPageable").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/role/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/users/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/products/**").hasAnyAuthority("ROLE_ADMIN");

        /*http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("ROLE_ADMIN");*/
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}

package com.gcoder.securityflux.config;

import com.gcoder.securityflux.base.*;
import com.gcoder.securityflux.base.impl.*;
import com.gcoder.securityflux.base.impl.AntiDDOSFilter;
import com.gcoder.securityflux.base.impl.UserAuthenticationToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetailsServiceMemory userDetailsServiceMemory = new UserDetailsServiceMemory();
        userDetailsServiceMemory.addUserDetails(new UserDetailImpl("ninh", "1234", Set.of("user"), true));
        userDetailsServiceMemory.addUserDetails(new UserDetailImpl("anh", "1234", Set.of("admin"), true));
        userDetailsServiceMemory.addUserDetails(new UserDetailImpl("admin", "1234", Set.of("user"), true));
        return userDetailsServiceMemory;
    }
    @Bean
    public JwtService jwtService(){
        return new JwtServiceImpl(1000*60*60*4);
    }

    @Bean
    public UserAuthentication userAuthentication(UserDetailsService userDetailsService, JwtService jwtService){
        return new UserAuthenticationToken(userDetailsService, jwtService);
    }
    @Bean
    public ServerHttpSecurity serverHttpSecurity(){
        return new ServerHttpSecurityImpl();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, UserAuthentication userAuthentication) {
        return http.authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/hello/**", "/api/**").permitAll() // khong can co tai khoan van pass, add toan bo uri vao list<String> All
                        .pathMatchers("/hello/**").authenticated() // chi can co tai khoan la qua, add toan bo uri vao list<String> authen
                        .pathMatchers("/hello/**").hasAnyRole("admin","user") // co tai khoan va co role yeu cau, add vao map<String, String[]> role
                )
                .addUserAuthenticationAt(userAuthentication)
                .addFilterAt(new AntiDDOSFilter(10,10))
                .build();
    }
}

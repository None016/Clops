package com.example.Clops.config;


import com.example.Clops.service.security.JwtAuthenticationFilter;
import com.example.Clops.service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/territories/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/territories").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/territories/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/territories/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/spatial-objects/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/spatial-objects").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/spatial-objects/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/spatial-objects/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/fiber-connections/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/fiber-connections").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/fiber-connections/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/fiber-connections/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/fiber-connections/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

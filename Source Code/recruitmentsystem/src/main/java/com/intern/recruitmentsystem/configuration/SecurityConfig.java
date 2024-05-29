package com.fpt.recruitmentsystem.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.recruitmentsystem.service.implement.CustomOauth2UserService;
import com.fpt.recruitmentsystem.service.implement.Oauth2LoginSuccessService;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomOauth2UserService customOauth2UserService;
    private final Oauth2LoginSuccessService loginSuccessHandler;

    private static final String ADMIN = "ADMIN";
    private static final String CANDIDATE = "CANDIDATE";
    private static final String RECRUITER = "RECRUITER";
    private static final String INTERVIEWER = "INTERVIEWER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**",
                                "/login",
                                "/oauth2/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/v2/api-docs/**",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**").permitAll()
                        // due to the next request matchers allow all 'candidates' GET
                        // we have to define the exception endpoints first
                        .requestMatchers(HttpMethod.GET, 
                                "/candidates",
                                "/candidates/vacancy-list",
                                "/candidates/cv",
                                "/candidates/cv/{cvId}",
                                "/candidates/interviews",
                                "/candidates/interviews/{interviewId}").hasAuthority(CANDIDATE)
                        .requestMatchers(HttpMethod.GET,
                                "/levels/**",
                                "/positions/**",
                                "/skills/**",
                                "/vacancies/**",
                                "/events/**",
                                "/candidates/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/events/apply/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/admins/**").hasAuthority(ADMIN)
                        .requestMatchers("/recruiters/**").hasAnyAuthority(RECRUITER, ADMIN)
                        .requestMatchers("/interviewers/**").hasAnyAuthority(INTERVIEWER, ADMIN)
                        .requestMatchers("/candidates/**").hasAnyAuthority(CANDIDATE, ADMIN)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOauth2UserService))
                        .successHandler(loginSuccessHandler)
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    private AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ResponseMessage responseMessage = new ResponseMessage("Access Denied");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
        };
    }
}

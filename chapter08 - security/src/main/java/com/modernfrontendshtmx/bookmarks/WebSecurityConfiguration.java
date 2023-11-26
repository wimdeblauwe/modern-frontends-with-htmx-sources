package com.modernfrontendshtmx.bookmarks;


import io.github.wimdeblauwe.htmx.spring.boot.security.HxRefreshHeaderAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) { // <.>
        UserDetails userDetails = User.builder()
                .username("admin") // <.>
                .password(encoder.encode("admin")) // <.>
                .build();
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(userDetails);
        return inMemoryUserDetailsManager;
    }

    // tag::filterChain[]
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var entryPoint = new HxRefreshHeaderAuthenticationEntryPoint(); // <.>
        var requestMatcher = new RequestHeaderRequestMatcher("HX-Request"); // <.>
        return http
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(exception ->
                        exception.defaultAuthenticationEntryPointFor(entryPoint,
                                requestMatcher)) // <.>
                .build();
    }
    // end::filterChain[]
}

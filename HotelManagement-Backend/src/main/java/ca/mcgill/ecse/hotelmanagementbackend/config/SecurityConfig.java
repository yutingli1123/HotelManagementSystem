package ca.mcgill.ecse.hotelmanagementbackend.config;

import ca.mcgill.ecse.hotelmanagementbackend.config.filter.ApiKeyAuthFilter;
import ca.mcgill.ecse.hotelmanagementbackend.enumeration.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return authentication -> {
            String principal = (String) authentication.getPrincipal();
            String[] formattedToken = principal.split(" ");
            if (formattedToken[0].equals("Bearer")) {
                Role role = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getClaim("role").as(Role.class);
                String username = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(formattedToken[1]).getIssuer();
                if (role != null) {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    authentication = new PreAuthenticatedAuthenticationToken(username, principal, authorities);
                    authentication.setAuthenticated(true);
                }
            }
            return authentication;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        ApiKeyAuthFilter filter = new ApiKeyAuthFilter(HttpHeaders.AUTHORIZATION);
        filter.setAuthenticationManager(customAuthenticationManager());
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> corsConfigurationSource())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(filter)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/register")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/refresh")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers/name")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/customers/me")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/update")
                        .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/customers/update/password")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reservations/book")
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reservations/cancel/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/rooms/available")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/rooms/available/type")
                        .permitAll()
                        .anyRequest()
                        .hasAnyAuthority("ROLE_OWNER", "ROLE_EMPLOYEE")
                );
        return http.build();
    }
}

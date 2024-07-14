package com.increff.chakra.config;

import com.increff.account.client.AuthTokenFilter;
import com.increff.account.client.CredentialFilter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CredentialFilter credentialFilter;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    public static final String ADMIN = "app.admin";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http//
                // Match only these URLs
                .requestMatchers()//
                .antMatchers("/**")//
                .and()
                .authorizeRequests()
                .anyRequest()
                .hasAnyAuthority(ADMIN)
                // Ignore CSRF and CORS
                .and().csrf().disable()
                .addFilterBefore(authTokenFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(credentialFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        log.info("Configuration complete");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-resources", "/swagger-ui.html", "/webjars/**","/configuration/ui",
                "/configuration/security","/v2/api-docs","/session/**","/misc/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

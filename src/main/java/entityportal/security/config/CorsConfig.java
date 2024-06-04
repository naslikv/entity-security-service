package entityportal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
//This can be used in combination with @CrossOrigin on the controller & method.

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "OPTIONS", "GET","POST","PUT","PATCH","DELETE")
                        .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept","Authorization");
            }
        };
    }
}
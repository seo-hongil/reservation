package project.toy.reservation.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadPath + "/";
        // 브라우저에서 /display/stores/** 로 접근하면 C:/upload/stores/ 에서 찾아라!
        registry.addResourceHandler("/display/**")
                .addResourceLocations(location);
    }
}

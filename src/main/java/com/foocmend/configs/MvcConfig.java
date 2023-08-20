package com.foocmend.configs;

import com.foocmend.commons.interceptors.CommonInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing
public class MvcConfig implements WebMvcConfigurer {
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @Autowired
    private CommonInterceptor commonInterceptor; // 공통 인터셉터

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 파일 업로드 정적 경로 설정 */
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + fileUploadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 공통 인터셉터 추가
        registry.addInterceptor(commonInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    public MessageSource messageSource() {

        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setDefaultEncoding("UTF-8");
        ms.addBasenames("messages.commons", "messages.validations", "messages.errors");

        return ms;
    }
}

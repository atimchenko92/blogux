package de.hska.lkit.blogux.session;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages = { "de.hska.lkit.blogux" })
public class WebConfig extends WebMvcConfigurerAdapter {

  @Bean
  public BloguxCookieInterceptor bloguxCookieInterceptor() {
    return new BloguxCookieInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(bloguxCookieInterceptor()).addPathPatterns("/login/**");
  }
}

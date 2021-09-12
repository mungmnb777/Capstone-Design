package graduation.mazerunner;

import graduation.mazerunner.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/signup", "/members/login", "/members/logout",
                        "/maps/list", "/maps/list/{mapId}", "/board/list", "/board/list/{postId}",
                        "/css/**", "/error");
    }
}

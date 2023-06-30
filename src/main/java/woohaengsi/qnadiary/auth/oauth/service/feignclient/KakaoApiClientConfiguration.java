package woohaengsi.qnadiary.auth.oauth.service.feignclient;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class KakaoApiClientConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    }

}

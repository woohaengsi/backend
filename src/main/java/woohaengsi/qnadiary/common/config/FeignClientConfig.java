package woohaengsi.qnadiary.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("woohaengsi.qnadiary")
public class FeignClientConfig {

}

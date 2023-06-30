package woohaengsi.qnadiary.auth.oauth.service.feignclient;

import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import woohaengsi.qnadiary.auth.oauth.dto.AccessTokenResponse;
import woohaengsi.qnadiary.auth.oauth.dto.KakaoUserInfo;

@FeignClient(name = "kakao-api-client", configuration = {KakaoApiClientConfiguration.class}, url = "www.philsogood.com")
public interface KakaoOAuthApiClient {

    @PostMapping
    AccessTokenResponse requestAccessToken(URI tokenUrl, @RequestBody MultiValueMap<String, String> body);

    @GetMapping
    KakaoUserInfo requestUserInfo(URI userInfoUrl, @RequestHeader("Authorization") String authorization);
}

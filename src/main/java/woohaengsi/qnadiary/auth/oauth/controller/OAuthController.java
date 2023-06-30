package woohaengsi.qnadiary.auth.oauth.controller;

import static woohaengsi.qnadiary.auth.utils.OAuthUtils.ACCESS_TOKEN;
import static woohaengsi.qnadiary.auth.utils.OAuthUtils.REFRESH_TOKEN;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.auth.oauth.dto.LoginMemberInfo;
import woohaengsi.qnadiary.auth.oauth.dto.OAuthAccessToken;
import woohaengsi.qnadiary.auth.oauth.properties.OAuthPropertiesMapper;
import woohaengsi.qnadiary.auth.oauth.service.OAuthService;
import woohaengsi.qnadiary.auth.service.LoginService;
import woohaengsi.qnadiary.member.domain.Member;

@RestController
public class OAuthController {

    private final OAuthPropertiesMapper mapper;
    private final Map<String, OAuthService> oAuthServiceMap;
    private final LoginService loginService;
    private final JwtProvider jwtProvider;

    public OAuthController(OAuthPropertiesMapper mapper, Map<String, OAuthService> oAuthServiceMap,
        LoginService loginService, JwtProvider jwtProvider) {
        this.mapper = mapper;
        this.oAuthServiceMap = oAuthServiceMap;
        this.loginService = loginService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/login/{resource-server}/form")
    public void redirectLoginForm(HttpServletResponse response,
        @PathVariable(name = "resource-server") String resourceServer) throws IOException {
        if (!oAuthServiceMap.containsKey(resourceServer)) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        }
        String loginFormUrl = mapper.getOAuthProperties(resourceServer).loginFormUrl();
        response.sendRedirect(loginFormUrl);
    }

    @PostMapping("/login/{resource-server}")
    public LoginMemberInfo login(@PathVariable(name = "resource-server") String resourceServer,
        @RequestBody Map<String, String> map, HttpServletResponse response) {

        OAuthService oAuthService = oAuthServiceMap.get(resourceServer);
        String code = map.get("code");
        if (code == null) {
            throw new IllegalArgumentException("Authorization Code가 비어있습니다.");
        }

        OAuthAccessToken oAuthAccessToken = oAuthService.requestAccessToken(code);
        Member oAuthUser = oAuthService.requestUserInfo(oAuthAccessToken);
        Member loginMember = loginService.login(oAuthUser);

        String jwtAccessToken = jwtProvider.issueAccessToken(loginMember.getId());
        String jwtRefreshToken = jwtProvider.issueRefreshToken(loginMember.getId());
        loginService.updateRefreshToken(jwtRefreshToken, loginMember.getId());

        response.setHeader(ACCESS_TOKEN, jwtAccessToken);
        response.setHeader(REFRESH_TOKEN, jwtRefreshToken);
        return LoginMemberInfo.of(loginMember);
    }

    @GetMapping("/reissue/access-token")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        String reissuedAccessToken = jwtProvider.issueAccessToken(decodedMemberId);

        response.setHeader(ACCESS_TOKEN, reissuedAccessToken);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        loginService.deleteRefreshToken(decodedMemberId);
    }

    @DeleteMapping("/member/withdrawal")
    public void withdrawal(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        loginService.withdraw(decodedMemberId);
    }
}

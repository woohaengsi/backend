package woohaengsi.qnadiary.auth.oauth.controller;

import static woohaengsi.qnadiary.auth.utils.OAuthUtils.ACCESS_TOKEN;
import static woohaengsi.qnadiary.auth.utils.OAuthUtils.REFRESH_TOKEN;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.auth.oauth.dto.LoginMemberInfo;
import woohaengsi.qnadiary.auth.oauth.dto.OAuthAccessToken;
import woohaengsi.qnadiary.auth.oauth.properties.OAuthPropertiesMapper;
import woohaengsi.qnadiary.auth.oauth.service.OAuthService;
import woohaengsi.qnadiary.auth.service.LoginService;
import woohaengsi.qnadiary.member.domain.Member;

@Tag(name = "소셜 로그인", description = "소셜 로그인 관련 API")
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

    @Operation(summary = "CallBack URL", description = "로그인 시도 시 해당 URL로 Authorization code를 발급한다.")
    @GetMapping("/login/{resource-server}/callback")
    public void loginCallback(@RequestParam String authorizationCode) throws IOException {

    }

    @Operation(summary = "로그인 리다이렉트 (삭제 예정)", description = "유저가 동의하도록 리다이렉트 해준다.")
    @GetMapping("/login/{resource-server}/form")
    public void redirectLoginForm(HttpServletResponse response,
        @PathVariable(name = "resource-server") String resourceServer) throws IOException {
        if (!oAuthServiceMap.containsKey(resourceServer)) {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        }
        String loginFormUrl = mapper.getOAuthProperties(resourceServer).loginFormUrl();
        response.sendRedirect(loginFormUrl);
    }

    @Operation(summary = "로그인 요청(수정 예정)", description = "Authorizaiton code로 로그인 요청")
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

    @Operation(summary = "토큰 재발급 요청", description = "토큰 만료시 리프레쉬 토큰으로 재요청한다.")
    @GetMapping("/reissue/access-token")
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        String reissuedAccessToken = jwtProvider.issueAccessToken(decodedMemberId);

        response.setHeader(ACCESS_TOKEN, reissuedAccessToken);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 시 서버에 저장된 리프레쉬 토큰 삭제")
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        loginService.deleteRefreshToken(decodedMemberId);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 정보와 관련 글을 모두 삭제(Hard Delete)")
    @DeleteMapping("/member/withdrawal")
    public void withdrawal(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);
        Long decodedMemberId = jwtProvider.decode(refreshToken);
        loginService.withdraw(decodedMemberId);
    }
}

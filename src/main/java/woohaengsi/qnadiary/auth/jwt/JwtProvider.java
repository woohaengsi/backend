package woohaengsi.qnadiary.auth.jwt;

import static javax.management.timer.Timer.ONE_HOUR;
import static javax.management.timer.Timer.ONE_WEEK;
import static woohaengsi.qnadiary.auth.utils.OAuthUtils.ACCESS_TOKEN;
import static woohaengsi.qnadiary.auth.utils.OAuthUtils.REFRESH_TOKEN;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final String issuer;
    private final Algorithm algorithm;

    public JwtProvider(JwtProperties jwtProperties) {
        this.issuer = jwtProperties.getIssuer();
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey());
    }

    public String issueAccessToken(Long memberId) {
        return issueToken(ACCESS_TOKEN, memberId, Date.from(Instant.now().plusMillis(ONE_HOUR)));
    }

    public String issueRefreshToken(Long memberId) {
        return issueToken(REFRESH_TOKEN, memberId, Date.from(Instant.now().plusMillis(ONE_WEEK * 2)));
    }

    public Long decode(String token) {
        return JWT.decode(token)
            .getClaim("memberId")
            .asLong();
    }

    public void verifyToken(String token) {
        JWT.require(algorithm)
            .withIssuer(issuer)
            .withSubject(ACCESS_TOKEN)
            .build()
            .verify(token);
    }

    private String issueToken(String subject, Long memberId, Date expiresAt) {
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(subject)
            .withAudience(memberId.toString())
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(expiresAt)

            .withClaim("memberId", memberId)

            .sign(algorithm);
    }
}

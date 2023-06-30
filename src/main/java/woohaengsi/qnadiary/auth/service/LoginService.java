package woohaengsi.qnadiary.auth.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;

@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(Member oauthUser) {
        Optional<Member> findMember = memberRepository.findMemberByResourceServerAndResourceServerId(
            oauthUser.getResourceServer(), oauthUser.getResourceServerId());

        if (findMember.isEmpty()) {
            return memberRepository.save(oauthUser);
        }
        return findMember.get();
    }

    public void updateRefreshToken(String newRefreshToken, Long memberId) {
        Member findMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        findMember.updateRefreshToken(newRefreshToken);
    }

    public void deleteRefreshToken(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        findMember.updateRefreshToken(null);
    }

    public void withdraw(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        memberRepository.delete(findMember);
    }
}

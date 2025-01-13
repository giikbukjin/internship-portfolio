package internship.portfolio.member.service;

import internship.portfolio.member.entity.Member;
import internship.portfolio.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    // username 검색해 사용자가 존재하는지 확인
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    // user 데이터 존재하면 UserDetails 객체로 만들어 반환
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                // DB에 encoding된 pw 값을 저장하는 것이 좋지만, 우선은 검증 객체를 생성할 때 encoding 했다.
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}

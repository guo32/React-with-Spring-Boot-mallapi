package org.dorastudy.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.dorastudy.mallapi.domain.Member;
import org.dorastudy.mallapi.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@abc.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("USER" + i)
                    .build();

            member.addRole(MemberRole.USER);

            if (i == 0) {
                member.addRole(MemberRole.ADMIN);
            }

            if(i <= 5) {
                member.addRole(MemberRole.MANAGER);
            }

            memberRepository.save(member);
        }
    }

    @Test
    public void testRead() {
        String email = "user0@abc.com";

        Member member = memberRepository.getWithRoles(email);

        log.info(member.toString());
        log.info(member.getMemberRoleList());
    }
}

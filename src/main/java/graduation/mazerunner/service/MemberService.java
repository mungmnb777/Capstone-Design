package graduation.mazerunner.service;

import graduation.mazerunner.domain.Member;
import graduation.mazerunner.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public String join(Member member) {
        // 중복 회원 검증
        Member findMember = memberRepository.findOne(member.getId());
        if (findMember != null) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        // 비밀번호 일치 여부 검증
        if (!member.getPassword().equals(member.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member login(Member member) {
        // 아이디 검증
        Member findMember = memberRepository.findOne(member.getId());


        if (findMember == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        // 비밀번호 검증
        if (!findMember.getPassword().equals(member.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 다시 한번 확인해주세요");
        }

        return findMember;
    }

    public Member findOne(String id) {
        return memberRepository.findOne(id);
    }

    public List<Member> findByIdOrNicknameContaining(String type, String value) {
        List<Member> findMemberList;

        if (type.equals("id")) {
            findMemberList = memberRepository.findByIdContaining(value);
        } else if (type.equals("nickname")) {
            findMemberList = memberRepository.findByNicknameContaining(value);
        } else {
            findMemberList = new ArrayList<>();
        }


        return findMemberList;
    }
}

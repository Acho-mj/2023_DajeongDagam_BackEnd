package com.dagagam.dagagamweb.service;

import com.dagagam.dagagamweb.constant.MemberAuthority;
import com.dagagam.dagagamweb.dto.MemberFormDto;
import com.dagagam.dagagamweb.dto.MemberPwdDto;
import com.dagagam.dagagamweb.dto.MemberReturnIdDto;
import com.dagagam.dagagamweb.entity.Member;
import com.dagagam.dagagamweb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(@Lazy MemberRepository memberRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member registerMember(MemberFormDto memberFormDto) {
        Member member = Member.builder()
                .email(memberFormDto.getEmail())
                .name(memberFormDto.getName())
                .password(memberFormDto.getPassword())
                .memberAuthority(MemberAuthority.ROLE_USER)
                .build();
        MemberFormDto memberDto = new MemberFormDto(member);

        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Member member, MemberFormDto memberFormDto) {
        Optional<Member> byId = memberRepository.findById(member.getId());
        Member me = byId.orElseThrow();

        me.setName(memberFormDto.getName());
        me.setPassword(memberFormDto.getPassword());
        memberRepository.save(me);

        return me;
    }

    @Transactional
    public MemberFormDto getMember(Member member) {
        Optional<Member> byId = memberRepository.findById(member.getId());
        Member me = byId.orElseThrow();

        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email(me.getEmail())
                .name(me.getName())
                .build();

        return memberFormDto;
    }

    @Transactional
    public MemberFormDto getMember(Long no) {
        Optional<Member> byId = memberRepository.findById(no);
        Member me = byId.orElseThrow();

        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email(me.getEmail())
                .name(me.getName())
                .build();

        return memberFormDto;
    }

    public MemberReturnIdDto getMemberId(String email) {
        Member member = memberRepository.findByEmail(email);
        MemberReturnIdDto memberReturnIdDto = MemberReturnIdDto.builder()
                .id(member.getId())
                .build();

        return memberReturnIdDto;
    }

//    private void validateDuplicateMember(Member member) {
//        Member findMember = memberRepository.findByEmail(member.getEmail());
//        if (findMember != null) {
//            throw new IllegalStateException("이미 가입된 이메일입니다.");
//        }
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member==null) {
            throw new UsernameNotFoundException(email);
        }

        return new CustomUserDetail(member);
    }

    @Transactional
    public Member updatePwd(Member member, MemberPwdDto memberPwdDto) throws Exception {
        Optional<Member> byId = memberRepository.findById(member.getId());
        Member me = byId.orElseThrow();

        if (me.matchPassword(passwordEncoder, memberPwdDto.getOriginal())) {
            if (memberPwdDto.getNewpwd1().equals(memberPwdDto.getNewpwd2())) {
                me.updatePassword(passwordEncoder, memberPwdDto.getNewpwd1());
            } else {
                throw new Exception();
            }
        }
        else {
            throw new Exception();
        }
        return me;
    }
}


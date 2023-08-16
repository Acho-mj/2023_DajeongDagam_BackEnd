package com.dagagam.dagagamweb.controller;

import com.dagagam.dagagamweb.dto.MemberFormDto;
import com.dagagam.dagagamweb.dto.MemberLoginDto;
import com.dagagam.dagagamweb.dto.MemberPwdDto;
import com.dagagam.dagagamweb.dto.MemberReturnIdDto;
import com.dagagam.dagagamweb.entity.Member;
import com.dagagam.dagagamweb.repository.MemberRepository;
import com.dagagam.dagagamweb.service.CustomUserDetail;
import com.dagagam.dagagamweb.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

//@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // 가입
    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody MemberFormDto memberFormDto) {
        memberFormDto.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        Member registeredMember = memberService.registerMember(memberFormDto);
        return new ResponseEntity<>(registeredMember, HttpStatus.OK);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberReturnIdDto> login(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody MemberLoginDto memberLoginDto){
        UserDetails userDetails = memberService.loadUserByUsername(memberLoginDto.getEmail());
        MemberReturnIdDto memberReturnIdDto = memberService.getMemberId(memberLoginDto.getEmail());
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(userDetails, memberLoginDto.getPassword(), new ArrayList<>());
        try {
            authenticationManager.authenticate(authentication);
        }
        catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid email or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession();
        session.setAttribute
                (HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        cookie.setMaxAge(30000 * 60);
        cookie.setSecure(true);
        response.addCookie(cookie);
//        return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity<>(memberReturnIdDto, HttpStatus.OK);
    }

    // 회원정보
    @GetMapping("/info")
    public ResponseEntity<MemberFormDto> getUserInfo(@AuthenticationPrincipal CustomUserDetail customUserDetail) {
        MemberFormDto memberReturn = memberService.getMember(customUserDetail.getMember());

        return ResponseEntity.status(HttpStatus.OK).body(memberReturn);
    }

    @GetMapping("/info/{no}")
    public ResponseEntity<MemberFormDto> getUserInfo(@PathVariable("no") Long no) {
        MemberFormDto memberReturn = memberService.getMember(no);

        return ResponseEntity.status(HttpStatus.OK).body(memberReturn);
    }

    @PutMapping("/info/edit")
    public ResponseEntity<MemberFormDto> updateUserInfo(@AuthenticationPrincipal CustomUserDetail customUserDetail, @RequestBody MemberFormDto memberFormDto) {
        Member member = memberService.updateMember(customUserDetail.getMember(), memberFormDto);
        MemberFormDto memberReturn = memberService.getMember(member);

        return ResponseEntity.ok(memberReturn);
    }

    @PostMapping("/info/changepwd")
    public ResponseEntity<HttpStatus> changePwd(@AuthenticationPrincipal CustomUserDetail customUserDetail, @RequestBody MemberPwdDto memberPwdDto) {
        try {
            memberService.updatePwd(customUserDetail.getMember(), memberPwdDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}


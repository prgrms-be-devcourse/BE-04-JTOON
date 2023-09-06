package com.devtoon.jtoon.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.member.application.MemberService;
import com.devtoon.jtoon.member.request.SignUpReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignUpReq signUpReq) {
        memberService.createMember(signUpReq);
    }

    @GetMapping("/email-authorization")
    public String authenticateEmail(@RequestParam(value = "email") String email) {
        return memberService.sendEmailAuthentication(email);
    }
}

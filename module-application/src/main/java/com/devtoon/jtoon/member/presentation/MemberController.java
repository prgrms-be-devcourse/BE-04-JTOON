package com.devtoon.jtoon.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devtoon.jtoon.member.application.MemberService;
import com.devtoon.jtoon.member.request.SignUpDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        memberService.createMember(signUpDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/email-authorize")
    public ResponseEntity<String> authenticateEmail(@RequestParam(value = "email", required = true) String email) {
        String authenticationUuid = memberService.sendEmailAuthentication(email);

        return ResponseEntity.status(HttpStatus.OK).body(authenticationUuid);
    }
}

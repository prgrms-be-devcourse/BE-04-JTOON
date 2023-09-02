package com.devtoon.jtoon.member.presentation;

import com.devtoon.jtoon.member.application.MemberService;
import com.devtoon.jtoon.member.request.SignUpDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

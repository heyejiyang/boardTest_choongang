package com.bbYang.member.controllers;

import com.bbYang.member.services.MemberSaveService;
import com.bbYang.member.validators.JoinValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form)
    {
        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors){

        joinValidator.validate(form,errors);
        if(errors.hasErrors()){
            return "front/member/join";
        }

        memberSaveService.save(form);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(){
        return "front/member/login";
    }

}

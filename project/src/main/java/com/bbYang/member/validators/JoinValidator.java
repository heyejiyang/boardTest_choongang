package com.bbYang.member.validators;

import com.bbYang.global.validators.MobileValidator;
import com.bbYang.global.validators.PasswordValidator;
import com.bbYang.member.controllers.RequestJoin;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JoinValidator implements Validator, PasswordValidator, MobileValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //target에는 커맨드 객체 넘어온다.
        if(errors.hasErrors()){
            return;
        }
        RequestJoin form = (RequestJoin) target;
        String email = form.getEmail();
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String mobile = form.getMobile();

        /**
         * 1. 이미 가입된 회원인지 체크
         * 2. 비밀번호, 비밀번호 확인 일치 여부
         * 3. 비밀번호 복잡성 체크
         * 4. 휴대전화번호 형식 체크
         */

        //2. 비밀번호, 비밀번호확인 일치여부
        if(!password.equals(confirmPassword)){
            errors.rejectValue("confirmPassword","Mismatch.password");
        }

        //3. 비밀번호 복잡성 체크 - 알파벳 대소문자, 숫자, 특수문자 1개 이상씩
        if(!alphaCheck(password,false)/*대소문자 모두 포함되지 않았을경우*/ || !numberCheck(password) || !specialCharCheck(password)){
            errors.rejectValue("password" ,"Complexity.requestJoin.password");
        }

        //4. 휴대전화번호 형식 체크
        if(!mobileCheck(mobile)){
            errors.rejectValue("mobile","Mobile");
        }

    }
}

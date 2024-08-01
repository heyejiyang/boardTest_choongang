package com.bbYang.global.validators;

public interface MobileValidator {
    default boolean mobileCheck(String mobile){
        /**
         * 01[016]-0000/000-0000
         * 01[016]-\d{3,4}-\d{4}
         * 010.1111.1111
         * 010 1111 1111
         * 010-1111-1111
         * 01011111111
         * 1. 숫자만 남긴다. 2. 패턴 만들기 3. 체크
         */

        mobile = mobile.replaceAll("\\D",""); //숫자가 아닌 패턴 제거, \\D: 숫자가 아닌 문자 하나
        String pattern = "01[016]\\d{3,4}\\d{4}$";
        // 문자열이 01로 시작하고 0마지막 세번째 숫자는 0,1,6중에서 하나와 일치해야한다.
        // \\d{3,4}: 숫자가 세번에서 네번 반복됨 즉, 세자리 혹은 네자리
        // \\d{4}: 숫자가 네번 반복된 즉, 네자리
        //$ 끝나는 패턴
        return mobile.matches(pattern);
    }
}

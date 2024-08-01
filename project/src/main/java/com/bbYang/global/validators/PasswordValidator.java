package com.bbYang.global.validators;

public interface PasswordValidator {
    /**
     * 알파벳 복잡성 체크
     * @param password
     * @param caseInsensitive - false: 대소문자 각각 1개씩 이상 포함, true: 대소문자 구분 X
     * @return
     */
    default boolean alphaCheck(String password, boolean caseInsensitive){
        if(caseInsensitive){ //대소문자 구분 없이 알파벳 체크
            return password.matches(".*[a-zA-Z]+.*");
        }
        return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
    }

    /**
     * 숫자 복잡성 체크
     * @param password
     * @return
     */
    default boolean numberCheck(String password){
        return password.matches(".*\\d+.*");
    }
    //.: 임의의 문자 하나를 의미
    //*: 앞의 문자가 0번 이상 반복될 수 있음
    //.*: 문자가 있을수도 있고 없을수도 있다.
    //\\d: 숫자(0-9)를 의미
    //+: 앞의 문자가 1번 이상 반복될 수 있음을 의미

    /**
     * 특수문자 복잡성 체크
     * @param password
     * @return
     */
    default boolean specialCharCheck(String password){
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣]+.*"; //비밀번호에 특수문자 패턴 있는지 체크
        return password.matches(pattern);
        //[^ ...]: 대괄호 안에 있는 문자가 아닌 문자

    }
}

package com.bbYang.file.services;


import com.bbYang.MemberUtil;
import com.bbYang.file.constants.FileStatus;
import com.bbYang.file.entities.FileInfo;
import com.bbYang.file.repositories.FileInfoRepository;
import com.bbYang.global.exceptions.UnAuthorizedException;
import com.bbYang.member.entities.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileDeleteService {
    private final FileInfoService infoService;
    private final FileInfoRepository infoRepository;
    private final MemberUtil memberUtil; //권한 체크

    //본인 파일 삭제 가능, 관리자는 모두 삭제 가능하도록
    //비회원 글 작성시 -> 게시글 수정 삭제할때 비밀번호 부여, 인증수단 등록 - 인증 받은 상태일때 삭제 가능하도록(나중에 구현)

    //삭제 정보가 필요할때가 있음
    public FileInfo delete(Long seq) {
        FileInfo data = infoService.get(seq); //조회
        String filePath = data.getFilePath();
        String createdBy = data.getCreatedBy(); //업로드한 회원 이메일

        Member member = memberUtil.getMember(); //로그인 한 사용자 회원 정보

        if(!memberUtil.isAdmin() && StringUtils.hasText(createdBy) && memberUtil.isLogin() && !member.getEmail().equals(createdBy)){//관리자가 아니면서 회원이 올린 파일이 있고, 현재 로그인 상태, 로그인 사용자 이메일과 작성자가 일치하지 않을때 삭제 불가능 하도록
            throw new UnAuthorizedException(); //권한 없음 예외 던짐
        }

        /*삭제 권한 있는 상태*/

        //파일 삭제
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }

        //파일 정보 삭제
        infoRepository.delete(data);
        infoRepository.flush();

        return data;
    }

    //파일 목록 삭제
    public List<FileInfo> delete(String gid, String location, FileStatus status){
        List<FileInfo> items = infoService.getList(gid, location, status); //전체 조회
        
        items.forEach(i -> delete(i.getSeq())); //일괄 삭제

        return items;
    }

    //상태 구분하지 않고 삭제
    public List<FileInfo> delete(String gid, String location){
        return delete(gid,location,FileStatus.ALL);
    }

    //gid만 가지고 삭제할경우
    public List<FileInfo> delete(String gid){
        return delete(gid,null);
    }


}

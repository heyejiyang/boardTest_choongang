package com.bbYang.file.services;


import com.bbYang.file.constants.FileStatus;
import com.bbYang.file.entities.FileInfo;
import com.bbYang.file.entities.QFileInfo;
import com.bbYang.file.exceptions.FileNotFoundException;
import com.bbYang.file.repositories.FileInfoRepository;
import com.bbYang.global.config.FileProperties;
import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService {
    private final FileInfoRepository infoRepository;
    private final FileProperties properties;
    private final HttpServletRequest request;
    /**
     * 파일 1개 조회
     * @param seq : 파일 등록 번호
     * @return
     */
    public FileInfo get(Long seq){
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new); //없을때 예외 발생

        /**
         * 2차 가공 - 추가적인 데이터 넣어줌
         * 1. 파일을 접근할 수 있는 URL - 보여주기 위한 목적
         * 2. 파일을 접근할 수 있는 PATH - 파일 삭제, 다운로드 등등
         */

        addFileInfo(item);

        return item;
    }

    /**
     * 파일 목록 조회
     * @param gid - 그룹 아이디
     * @param location - 파일 위치
     * @param status - ALL: 완료+미완료, DONE: 완료, UNDONE: 미완료
     * @return
     */
    public List<FileInfo> getList(String gid, String location, FileStatus status){
        //상세하게 전체 조회시
        // 완료된 파일과 완료되지 않은 파일 모두 보고 싶을 수도있음 - 상수정의
        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(fileInfo.gid.eq(gid));

        if (StringUtils.hasText(location)) {//location 값이 있을때 등록
            andBuilder.and(fileInfo.location.eq(location));
        }

        if(status != FileStatus.ALL){ //ALL이 아닐때
            andBuilder.and(fileInfo.done.eq(status == FileStatus.DONE));
            //DONE이면 완료된 파일만 조회, 아니면 미완료 파일만 조회
        }

        List<FileInfo> items = (List<FileInfo>) infoRepository.findAll(andBuilder, Sort.by(asc("createdAt")));//등록일자 기준 오름차순 정렬

        //2차 추가 데이터 처리
        items.forEach(this::addFileInfo);

        return items;
    }

    public List<FileInfo> getList(String gid, String location){
        return getList(gid, location, FileStatus.DONE); //DONE으로 고정
    }

    public List<FileInfo> getList(String gid){ //gid만 가지고 조회시
        return getList(gid, null, FileStatus.DONE); //DONE으로 고정
    }

    /**
     * 파일 정보 추가 처리
     * - fileUrl, filePath
     * @param item
     */
    public void addFileInfo(FileInfo item){
        String fileUrl = getFileUrl(item);
        String filePath = getFilePath(item);

        item.setFileUrl(fileUrl);
        item.setFilePath(filePath);
    }

    //브라우저 접근 주소
    public String getFileUrl(FileInfo item){
        return request.getContextPath() + properties.getUrl() + "/" + getFolder(item.getSeq()) + "/" + getFileName(item);
    }

    //서버 업로드 경로
    public String getFilePath(FileInfo item){
        return properties.getPath()+ "/" + getFolder(item.getSeq()) + "/" + getFileName(item);
    }

    public String getFolder(long seq){
        return String.valueOf(seq % 10L);
    }

    public String getFileName(FileInfo item){
        String fileName = item.getSeq() + Objects.requireNonNullElse(item.getExtension(),"");
        return fileName;
    }
}

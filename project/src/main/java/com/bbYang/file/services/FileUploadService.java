package com.bbYang.file.services;


import com.bbYang.file.entities.FileInfo;
import com.bbYang.file.repositories.FileInfoRepository;
import com.bbYang.global.config.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileUploadService{

    private final FileInfoRepository fileInfoRepository;
    private final FileInfoService fileInfoService;//후속처리 메서드가 정의되어있다.
    private final FileProperties properties;

    public List<FileInfo> upload(MultipartFile[] files, String gid, String location){
        /**
         * 1. 파일 정보 저장
         * 2. 파일을 서버로 이동
         * 3. 이미지 이면 썸네일 생성
         * 4. 업로드한 파일 목록 반환
         */
        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString();

        List<FileInfo> uploadedFiles = new ArrayList<>();

        //1. 파일 정보 저장
        for(MultipartFile file : files){
            String fileName = file.getOriginalFilename(); //실제 업로드한 파일 이름
            String contentType = file.getContentType(); //파일 형식

            //확장자 자르기
            String extension = fileName.substring(fileName.lastIndexOf(".")); //.부터 끝까지

            //DB에 저장
            FileInfo fileInfo = FileInfo.builder()
                    .gid(gid)
                    .location(location)
                    .fileName(fileName)
                    .contentType(contentType)
                    .extension(extension)
                    .build();

            fileInfoRepository.saveAndFlush(fileInfo);

            //2. 파일을 서버로 이동
            long seq = fileInfo.getSeq();
            String uploadDir = properties.getPath() +"/" + ( seq % 10L );

            File dir = new File(uploadDir);
            if(!dir.exists() || !dir.isDirectory()){ //아예 존재하지 않거나 디렉토리가 아닌 경우
                dir.mkdir();
            }
            String uploadPath = uploadDir + "/" + seq + extension;
            try{
                file.transferTo(new File(uploadPath));
                uploadedFiles.add(fileInfo); //업로드 성공 파일 정보
            }catch (IOException e){
                e.printStackTrace();
                //파일 이동 실패시 정보 삭제
                fileInfoRepository.delete(fileInfo);
                fileInfoRepository.flush();
            }
        }

        uploadedFiles.forEach(fileInfoService::addFileInfo);
        return uploadedFiles;
    }
}

package com.bbYang.file.services;


import com.bbYang.file.entities.FileInfo;
import com.bbYang.file.exceptions.FileNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FileDownloadService {
    private final FileInfoService infoService;
    private final HttpServletResponse response;

    public void download(Long seq){
        //파일 먼저 불러오고 응답헤더로 파일 다운로드 시키기
        FileInfo data = infoService.get(seq); //파일 1개 조회

        String filePath = data.getFilePath();
        String fileName = new String(data.getFileName().getBytes(),
                StandardCharsets.ISO_8859_1); //파일명으로 다운받기 때문에 이름도 필요함
        //한글은 3바이트 형태 - 깨지지 않게 2바이트 형태로 바꾸기

        //파일 없을때 예외 던지기
        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }

        String contentType = data.getContentType();
        //contentType 없을때 "application/octet-stream"고정
        contentType = StringUtils.hasText(contentType) ? contentType : "application/octet-stream";

        try(FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis)){
            //응답헤더
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName); //올렸던 파일명 그대로 다운로드
            response.setContentType(contentType);
            response.setIntHeader("Expires",0); //만료시간 없애야지 큰 파일도 문제 없이 다운받을 수 있다.
            response.setHeader("Cache-Control", "must-revalidate"); //캐시 통제
            response.setContentLengthLong(file.length()); //파일 용량

            OutputStream out = response.getOutputStream();
            //바이트 단위 출력
            out.write(bis.readAllBytes());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

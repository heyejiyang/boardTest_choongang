package com.bbYang.file.controllers;

import com.bbYang.file.entities.FileInfo;
import com.bbYang.file.services.FileDeleteService;
import com.bbYang.file.services.FileDownloadService;
import com.bbYang.file.services.FileInfoService;
import com.bbYang.file.services.FileUploadService;
import com.bbYang.global.exceptions.RestExceptionProcessor;
import com.bbYang.global.rests.JSONData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/*
@RequestBody
- Http 요청으로 넘어오는 바디 데이터 내용을 java 객체로 역직렬화
    application/json을 주고받을때 주로 사용함. multipart/form-data이 포함되는 경우는 사용 불가.

@RequestPart
- @RequestBody + multipart/form-data인 경우에 사용.
    RequestBody와 RequestPart는 HttpMessageConverter에 의해 동작하므로 Setter 없이 Object 생성됨.

@RequestParam
- multipart/form-data을 받아야 되는 경우에 사용 가능.
    1개의 HTTP 파라미터를 받을 때 사용.
    기본 설정으로 필요 여부가 필수로 되어있음.

@ModelAttribute
    @RequestPart와 유사하지만 동작 원리는 완전히 다르다.
    값에 직접적으로 접근할 수 있는 수단이 필요.
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements RestExceptionProcessor {

    private final FileUploadService uploadService;
    private final FileInfoService fileInfoService;
    private final FileDownloadService fileDownloadService;
    private final FileDeleteService fileDeleteService;

    @PostMapping("/upload")
    public ResponseEntity<JSONData> upload(@RequestPart("file") MultipartFile[] files, //매개변수로 파일 데이터 넘어옴, 어디 필드에서 넘어오는지 알려줘야함 -> RequestPart
                                           @RequestParam(name = "gid", required = false)String gid, @RequestParam(name = "location", required = false) String location){
        //@RequestPart는 multipart/form-data에 특화된 어노테이션
        List<FileInfo> items = uploadService.upload(files,gid,location);

        HttpStatus status = HttpStatus.CREATED;
        JSONData data = new JSONData(items);
        data.setStatus(status);

        return ResponseEntity.status(HttpStatus.CREATED).body(data);

    }

    
    @GetMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq){
        fileDownloadService.download(seq);
    }

    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq){
        FileInfo data = fileDeleteService.delete(seq); //낱개 삭제
        return new JSONData(data); //JSON 형태로 출력, 삭제된 파일 정보 활용할 때 있기 때문
        //rest형태로 출력한다.
    }

    @DeleteMapping("/deletes/{gid}")
    public JSONData deletes(@PathVariable("gid") String gid, @RequestParam(name = "location" ,required = false) String location){
        // gid는 필수, location은 필수 아님

        List<FileInfo> items =fileDeleteService.delete(gid, location); //여러개 삭제
        return new JSONData(items);
    }

    @GetMapping("/info/{seq}")
    public JSONData get(Long seq) {//개별 조회
        FileInfo data = fileInfoService.get(seq);
        return new JSONData(data);
    }
    @GetMapping("/iist/{gid}")
    public JSONData getList(@PathVariable("gid") String gid, @RequestParam(name = "location", required = false) String location) { //목록조회
        List<FileInfo> items = fileInfoService.getList(gid,location);
        return new JSONData(items);
    }


}

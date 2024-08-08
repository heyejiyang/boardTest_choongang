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

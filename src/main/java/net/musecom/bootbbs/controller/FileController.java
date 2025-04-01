package net.musecom.bootbbs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.bootbbs.entity.FileEntity;
import net.musecom.bootbbs.service.FileService;
import net.musecom.bootbbs.util.FileUploadUtil;

@RestController
public class FileController {

    private final FileService fileService;
    private final String uploadPath = "D:\\boot_works\\bootbbs\\src\\main\\resources\\static\\upload";

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Map<String, Object> fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileKey") Long fileKey) throws IOException {
        // 저장 Long bid, MultipartFile file, String uploadDir
        FileEntity entity = FileUploadUtil.saveFile(fileKey, file, uploadPath);

        // db 저장
        fileService.save(entity);

        Map<String, Object> data = new HashMap<>();
        data.put("fileId", entity.getId());
        data.put("nfilename", entity.getNewFilename());
        data.put("filesize", entity.getFileSize());
        data.put("fileUrl", "http://localhost:8081/upload/" + entity.getNewFilename());
        data.put("ext", entity.getExt());
        return data;
    }

}

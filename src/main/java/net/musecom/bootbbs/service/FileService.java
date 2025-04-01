package net.musecom.bootbbs.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.musecom.bootbbs.entity.FileEntity;
import net.musecom.bootbbs.repository.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final String uploadPath = "D:/boot_works/bootbbs/src/main/resources/static/upload/";

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // 파일저장
    public FileEntity save(FileEntity file) {
        return fileRepository.save(file);
    }

    // 특정 게시물에 소속된 파일 가져오기
    public List<FileEntity> getFilesByBid(Long bid) {
        return fileRepository.findByBid(bid);
    }

    // timestamp 등록된 bid 찾아서 id로 변경
    @Transactional
    public void updateBidByTempkey(Long tempBid, Long realBid) {
        List<FileEntity> files = fileRepository.findByBid(tempBid);
        for (FileEntity file : files) {
            file.setBid(realBid);
            fileRepository.save(file);
        }
    }

    @Transactional
    public boolean deleteFile(String nFilename) {
        Optional<FileEntity> fileOpt = fileRepository.findByNewFilename(nFilename);
        if (fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();

            // 실제 파일 삭제
            File realFile = new File(uploadPath + file.getNewFilename());
            if (realFile.exists()) {
                realFile.delete();
            }
            // db 삭제
            fileRepository.deleteById(file.getId());
            return true;
        }
        return false;
    }

    // 게시글 삭제 될때 소속된 파일도 삭제
    @Transactional
    public void deleteFilesByBid(Long bid) {
        List<FileEntity> files = fileRepository.findByBid(bid);
        for (FileEntity file : files) {
            deleteFile(file.getNewFilename());
        }
    }

}

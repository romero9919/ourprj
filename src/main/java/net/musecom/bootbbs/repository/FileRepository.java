package net.musecom.bootbbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.musecom.bootbbs.entity.FileEntity;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    // 파일목록
    List<FileEntity> findByBid(Long bid);

    // bid가 업데이트되지 (밀리초가 아직 들어있음) 않은 쓰레기 파일 목록
    @Query("SELECT f FROM FileEntity f WHERE f.bid > 1000000000")
    List<FileEntity> findByTrashFile();

    // 파일 이름으로 삭제
    Optional<FileEntity> findByNewFilename(String newFilename);
}

package pl.storex.storex.upload.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.storex.storex.upload.model.Upload;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Integer> {

    List<Upload> findByCreatedBy(Long userId);

}

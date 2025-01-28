package pl.storex.storex.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.storex.storex.upload.model.Upload;
import pl.storex.storex.upload.model.UploadDTO;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.service.UserRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;

    public List<Upload> getUploadsByUser(String username) {
        User user = userRepository.findUserByName(username);
        return uploadRepository.findByCreatedBy(user.getId());
    }

    public UploadDTO save(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        try {
            Upload build = Upload.builder()
                    .data(file.getBytes())
                    .createdBy(principal.getId())
                    .filename(file.getOriginalFilename())
                    .filesize(String.valueOf(file.getSize()))
                    .filetype(file.getContentType())
                    .created(Date.from(Instant.now()))
                    .updated(Date.from(Instant.now()))
                    .updatedBy(principal)
                    .description("Uploaded / Created by " + principal.getName())
                    .build();
            Upload save = uploadRepository.save(build);
            return UploadDTO.toDtoWithoutData(save);
        } catch (IOException e) {
            log.info("Error saving file: {}", e.getMessage());
            return null;
        }
    }

    public void delete(Upload upload) {
        uploadRepository.delete(upload);
    }

    public Upload getUploadById(Integer id) {
        return uploadRepository.findById(id).orElseThrow();
    }

    public Upload update(Upload upload) {
        return uploadRepository.save(upload);
    }

    public ResponseEntity<?> getUploadsByGroupOwner(String ownerEmail) {
        return ResponseEntity.ok(uploadRepository
                .findByCreatedBy(userRepository
                        .findUserByEmail(ownerEmail).orElseThrow().getId()));
    }
}

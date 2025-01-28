package pl.storex.storex.upload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.storex.storex.upload.model.UploadDTO;
import pl.storex.storex.upload.service.UploadService;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class UploadsController {

    private final UploadService uploadService;

    @PostMapping(path = "/upload")
    public ResponseEntity<?> upload(@RequestPart MultipartFile file) {
        return ResponseEntity.ok(uploadService.save(file));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        uploadService.delete(uploadService.getUploadById(id));
    }

    @GetMapping("/getUploadsByGroupOwner/{ownerEmail}")
    public ResponseEntity<?> getUploadsByGroupOwnerEmail(@PathVariable("ownerEmail") String ownerEmail) {
        return ResponseEntity.ok(uploadService.getUploadsByGroupOwner(ownerEmail));
    }

    //TODO: Add method to get uploads by user
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UploadDTO uploadDTO) {
        return ResponseEntity.ok(uploadService.update(uploadDTO.toEntity()));
    }

    @GetMapping("/getUploadById/{id}")
    public ResponseEntity<?> getUploadById(@PathVariable Integer id) {
        return ResponseEntity.ok(UploadDTO.toDTO(uploadService.getUploadById(id)));
    }
}

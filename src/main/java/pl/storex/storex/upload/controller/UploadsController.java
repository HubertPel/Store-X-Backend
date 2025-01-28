package pl.storex.storex.upload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.storex.storex.upload.model.UploadDTO;
import pl.storex.storex.upload.service.UploadService;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class UploadsController {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public void upload(@RequestBody UploadDTO uploadDTO) {
        uploadService.save(uploadDTO.toUpload());

    }
}

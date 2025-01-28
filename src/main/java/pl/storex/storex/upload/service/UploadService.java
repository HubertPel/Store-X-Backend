package pl.storex.storex.upload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.storex.storex.upload.model.Upload;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.service.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;

    public List<Upload> getUploadsByUser(String username) {
        User user = userRepository.findUserByName(username);
        return uploadRepository.findByCreatedBy(user.getId());
    }

    public Upload save(Upload upload) {
        return uploadRepository.save(upload);
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


}

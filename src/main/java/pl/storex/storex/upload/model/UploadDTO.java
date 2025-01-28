package pl.storex.storex.upload.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.storex.storex.user.model.User;
import pl.storex.storex.user.model.UserDTO;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String description;
    @NotBlank(message = "Invalid data: Empty data")
    @NotNull(message = "Invalid data: Null data")
    private byte[] data;
    @NotBlank(message = "Invalid filename: Empty filename")
    @NotNull(message = "Invalid filename: Null filename")
    private String filename;
    private String filesize;
    @NotBlank(message = "Invalid filetype: Empty filetype")
    @NotNull(message = "Invalid filetype: Null filetype")
    private String filetype;
    private Date created;
    @NotBlank(message = "Invalid createdBy: Empty createdBy")
    @NotNull(message = "Invalid createdBy: Null createdBy")
    private Long createdBy;
    private Date updated;
    @NotNull(message = "Invalid updatedBy: Null updatedBy")
    private UserDTO updatedBy;

    public Upload toEntity() {
        return Upload.builder()
                .id(this.id)
                .description(this.description)
                .data(this.data)
                .filename(this.filename)
                .filesize(this.filesize)
                .filetype(this.filetype)
                .created(this.created)
                .createdBy(this.createdBy)
                .updated(this.updated)
                .updatedBy(User.toUser(this.updatedBy))
                .build();
    }

    public static UploadDTO toDtoWithoutData(Upload  upload) {
        return UploadDTO.builder()
                .id(upload.getId())
                .description(upload.getDescription())
                .filename(upload.getFilename())
                .filesize(upload.getFilesize())
                .filetype(upload.getFiletype())
                .created(upload.getCreated())
                .createdBy(upload.getCreatedBy())
                .updated(upload.getUpdated())
                .updatedBy(UserDTO.builder()
                        .name(upload.getUpdatedBy().getName())
                        .email(upload.getUpdatedBy().getEmail())
                        .groupId(upload.getCreatedBy())
                        .id(upload.getUpdatedBy().getId())
                        .enabled(upload.getUpdatedBy().isEnabled())
                        .role(upload.getUpdatedBy().getRole())
                        .build())
                .build();
    }

    public static UploadDTO toDTO(Upload upload) {
        return UploadDTO.builder()
                .id(upload.getId())
                .description(upload.getDescription())
                .data(upload.getData())
                .filename(upload.getFilename())
                .filesize(upload.getFilesize())
                .filetype(upload.getFiletype())
                .created(upload.getCreated())
                .createdBy(upload.getCreatedBy())
                .updated(upload.getUpdated())
                .updatedBy(UserDTO.builder()
                        .name(upload.getUpdatedBy().getName())
                        .email(upload.getUpdatedBy().getEmail())
                        .groupId(upload.getUpdatedBy().getGroup_id())
                        .id(upload.getUpdatedBy().getId())
                        .enabled(upload.getUpdatedBy().isEnabled())
                        .role(upload.getUpdatedBy().getRole())
                        .build())
                .build();
    }

}

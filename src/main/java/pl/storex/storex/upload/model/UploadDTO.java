package pl.storex.storex.upload.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.storex.storex.user.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String description;
    private byte[] data;
    private String filename;
    private String filesize;
    private String filetype;
    private Instant created;
    private Long createdBy;
    private Instant updated;
    private User updatedBy;

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
                .updatedBy(this.updatedBy)
                .build();
    }

}

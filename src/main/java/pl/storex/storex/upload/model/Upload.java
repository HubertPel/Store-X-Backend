package pl.storex.storex.upload.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.storex.storex.user.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "uploads")
@NoArgsConstructor
@AllArgsConstructor
public class Upload implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

//    @Lob
    @Column(name = "data")
    private byte[] data;

    @Size(max = 50)
    @Column(name = "filename", length = 50)
    private String filename;

    @Size(max = 100)
    @Column(name = "filesize", length = 100)
    private String filesize;

//    @Size(max = 5)
    @Column(name = "filetype", length = 5)
    private String filetype;

//    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Date created;

    @Column(name = "created_by")
    private Long createdBy;

//    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    private Date updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

}
package pl.storex.storex.categories.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "categories")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date created_at;
    private Date updated_at;
    private Long updated_by;
    private Long created_by;
    private Long deleted_by;
    private Date deleted_at;

    public CategoryDTO toModel() {
        return CategoryDTO.builder()
                .name(this.name)
                .id(this.id)
                .created_at(this.created_at)
                .updated_at(this.updated_at)
                .updated_by(this.updated_by)
                .created_by(this.created_by)
                .deleted_by(this.deleted_by)
                .deleted_at(this.deleted_at)
                .build();
    }

    public static Category fromModel(CategoryDTO dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .created_at(dto.getCreated_at())
                .updated_at(dto.getUpdated_at())
                .updated_by(dto.getUpdated_by())
                .created_by(dto.getCreated_by())
                .deleted_by(dto.getDeleted_by())
                .deleted_at(dto.getDeleted_at())
                .build();
    }
}

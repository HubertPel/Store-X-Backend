package pl.storex.storex.categories.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Date created_at;
    private Date updated_at;
    private Long updated_by;
    private Long created_by;
    private Long deleted_by;
    private Date deleted_at;

}

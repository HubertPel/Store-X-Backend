package pl.storex.storex.group.model;


import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class UsersGroupDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String group_owner_email;
    private Date created_at;
    private Date updated_at;
    private Long updated_by;

}

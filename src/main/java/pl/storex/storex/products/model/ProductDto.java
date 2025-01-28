package pl.storex.storex.products.model;


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
public class ProductDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long categoryId;
    private String barcode;
    private Date createdAt;
    private Long createdBy;
    private Date updatedAt;
    private Long updatedBy;
    private Date deletedAt;
    private Long deletedBy;

}

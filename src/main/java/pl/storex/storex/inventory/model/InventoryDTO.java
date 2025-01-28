package pl.storex.storex.inventory.model;

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
public class InventoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private Long locationId;
    private Date addedAt;
    private Long addedBy;
    private Long updatedBy;
    private Date updatedAt;
    private Date expirationDate;

}

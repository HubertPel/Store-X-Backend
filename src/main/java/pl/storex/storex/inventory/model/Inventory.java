package pl.storex.storex.inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.storex.storex.user.service.UserService;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Inventory implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "location_id")
    private Long locationId;
    @Column(name = "added_at")
    private Date addedAt;
    @JoinTable(name = "appuser", joinColumns = @JoinColumn(name = "id", referencedColumnName = "added_by"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Long addedBy;
    @Column(name = "updated_by")
    private Long updatedBy;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "expiration_date", columnDefinition = "DATE")
    private Date expirationDate;

    public InventoryDTO toDTO() {
        return InventoryDTO.builder()
                .id(this.id)
                .productId(this.productId)
                .locationId(this.locationId)
                .addedAt(this.addedAt)
                .updatedBy(this.updatedBy)
                .updatedAt(this.updatedAt)
                .expirationDate(this.expirationDate)
                .build();
    }

    public static Inventory toEntity(InventoryDTO inventoryDto, UserService userService) {
        return Inventory.builder()
                .id(inventoryDto.getId())
                .productId(inventoryDto.getProductId())
                .locationId(inventoryDto.getLocationId())
                .addedBy(inventoryDto.getAddedBy())
                .addedAt(inventoryDto.getAddedAt())
                .updatedAt(inventoryDto.getUpdatedAt())
                .updatedBy(inventoryDto.getUpdatedBy())
                .expirationDate(inventoryDto.getExpirationDate())
                .build();
    }

}

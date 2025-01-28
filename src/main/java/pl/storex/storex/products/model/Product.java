package pl.storex.storex.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "category_id")
    private Long categoryId;
    private String barcode;
    @Column(name = "created")
    private Date createdAt;
    private Long createdBy;
    @Column(name = "updated")
    private Date updatedAt;
    private Long updatedBy;
    @Column(name = "deleted")
    private Date deletedAt;
    private Long deletedBy;

    public static Product toModel(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .id((productDto.getId() != null) ? productDto.getId() : null)
                .categoryId(productDto.getCategoryId())
                .barcode(productDto.getBarcode())
                .createdAt(productDto.getCreatedAt())
                .createdBy(productDto.getCreatedBy())
                .updatedAt(productDto.getUpdatedAt())
                .updatedBy(productDto.getUpdatedBy())
                .build();
    }

    public static ProductDto toDTO(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .barcode(product.getBarcode())
                .name(product.getName())
                .updatedBy(product.getUpdatedBy())
                .updatedAt(product.getUpdatedAt())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .deletedAt(product.getDeletedAt())
                .deletedBy(product.getDeletedBy())
                .build();
    }

}

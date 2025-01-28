package pl.storex.storex.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

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

    public static Product fromDTOtoModel(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .categoryId(productDto.getCategoryId())
                .barcode(productDto.getBarcode())
                .build();
        if(productDto.getId() != null) product.setId(productDto.getId());
        return product;
    }

    public ProductDto toDTOFromModel(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .barcode(product.getBarcode())
                .name(product.getName())
                .build();
    }

}

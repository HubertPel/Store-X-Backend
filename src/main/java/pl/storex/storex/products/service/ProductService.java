package pl.storex.storex.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.storex.storex.config.AuthUser;
import pl.storex.storex.products.model.Product;
import pl.storex.storex.products.model.ProductDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        List<Product> all;
        all = productRepository.findAll();
        return all;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public ResponseEntity<?> save(ProductDto productDto) {
        try {
            return productRepository.findByBarcode(productDto.getBarcode())
                    .map(existingProduct -> {
                        if (productDto.getBarcode().equals(existingProduct.getBarcode())) {
                            throw new InvalidDataAccessApiUsageException("Product with barcode: "
                                    + productDto.getBarcode() + " already exists");
                        }
                        return ResponseEntity.ok(Product.toDTO(existingProduct));
                    })
                    .orElseGet(() -> {
                        Product product = Product.toModel(productDto);
                        product.setUpdatedBy(AuthUser.currentUser().getId());
                        return ResponseEntity.ok(Product.toDTO(productRepository.save(product)));
                    });
        } catch (InvalidDataAccessApiUsageException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public void removeProduct(ProductDto productDto) {
        productRepository.findById(productDto.getId()).ifPresent(product -> {
            product.setDeletedBy(AuthUser.currentUser().getId());
            product.setUpdatedBy(AuthUser.currentUser().getId());
            productRepository.save(product);
        });
    }

    public Product findByBarcode(String barcode) {
        if (barcode != null) {
            return productRepository.findByBarcode(barcode).orElse(null);
        }
        return null;
    }

    public ProductDto updateProduct(ProductDto productDto) {
        Optional<Product> optionalProduct = Optional.empty();
        if (productDto.getId() != null) {
            optionalProduct = productRepository.findById(productDto.getId());
        }
        if (productDto.getBarcode() != null) {
            optionalProduct = Optional.ofNullable(findByBarcode(productDto.getBarcode()));
        }
        if (optionalProduct.isEmpty()) {
            Product product = Product.toModel(productDto);
            product.setUpdatedBy((productDto.getUpdatedBy() == null) ? AuthUser.currentUser().getId() : productDto.getCreatedBy());
            return Product.toDTO(productRepository.save(product));
        }
        optionalProduct.map(product -> {
            product.setBarcode(productDto.getBarcode());
            product.setUpdatedBy((productDto.getUpdatedBy() == null) ? AuthUser.currentUser().getId() : productDto.getCreatedBy());
            product.setCreatedBy((productDto.getCreatedBy() == null) ? AuthUser.currentUser().getId() : productDto.getCreatedBy());
            product.setName(productDto.getName());
            product.setCategoryId(productDto.getCategoryId());
            return Product.toDTO(productRepository.save(product));
        }).orElseThrow(() -> new InvalidDataAccessApiUsageException("Product not found"));
        return null;
    }

    public List<ProductDto> findByCategoryId(ProductDto dto) {
        Optional<List<Product>> byCategoryId = productRepository.getAllByCategoryId(dto.getCategoryId());
        if (byCategoryId.isPresent()) {
            List<ProductDto> productDtoArrayList = new ArrayList<>();
            for (Product product : byCategoryId.get()) {
                productDtoArrayList.add(Product.toDTO(product));
            }
            return productDtoArrayList;
        }
        return null;
    }
}

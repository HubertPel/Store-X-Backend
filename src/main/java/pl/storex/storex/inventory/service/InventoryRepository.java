package pl.storex.storex.inventory.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.storex.storex.inventory.model.Inventory;
import pl.storex.storex.inventory.model.InventoryDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<InventoryDTO> findByProductId(Long productId);

    Optional<InventoryDTO> findByAddedBy(Long addedBy);

    Optional<InventoryDTO> findByLocationId(Long locationId);

    List<InventoryDTO> findByExpirationDateBefore(Date expirationDateBefore);
}

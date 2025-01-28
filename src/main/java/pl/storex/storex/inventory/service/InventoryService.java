package pl.storex.storex.inventory.service;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.storex.storex.inventory.model.Inventory;
import pl.storex.storex.inventory.model.InventoryDTO;
import pl.storex.storex.user.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final UserService userService;


    @RolesAllowed("ROLE_ADMIN")
    public List<InventoryDTO> findAll() {
        return inventoryRepository.findAll()
                .stream().map(Inventory::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryDTO findById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(
                        () -> new RuntimeException("Inventory not found with id: " + id))
                .toDTO();
    }

    public Optional<InventoryDTO> findByProductId(Long id) {
        return inventoryRepository.findByProductId(id);
    }

    public Optional<InventoryDTO> findByLocationId(Long id) {
        return inventoryRepository.findByLocationId(id);
    }

    public Optional<InventoryDTO> findByAddedById(Long id) {
        return inventoryRepository.findByAddedBy(id);
    }

    public List<InventoryDTO> findByExpiredDate() {
        return inventoryRepository.findByExpirationDateBefore(Date.from(Instant.now()));
    }

    public Optional<InventoryDTO> update(InventoryDTO dto) {
        Optional<Inventory> inventory = inventoryRepository.findById(dto.getId());
        if (inventory.isPresent()) {
            Inventory entity = inventory.get();
            entity.setProductId(dto.getProductId());
            entity.setLocationId(dto.getLocationId());
            entity.setAddedBy(dto.getAddedBy());
            entity.setAddedAt(dto.getAddedAt());
            entity.setUpdatedBy(dto.getUpdatedBy());
            entity.setUpdatedAt(dto.getUpdatedAt());
            entity.setExpirationDate(dto.getExpirationDate());
            return Optional.of(inventoryRepository.save(entity).toDTO());
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }
}

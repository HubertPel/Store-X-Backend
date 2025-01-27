package pl.storex.storex.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.storex.storex.inventory.model.InventoryDTO;
import pl.storex.storex.inventory.service.InventoryService;

@Tag(name = "Store-X Inventory Controller")
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @Operation(summary = "Get all inventory items | Role = ADMIN")
    @GetMapping("/getAll")
    ResponseEntity<?> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/getById/{id}")
    ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(inventoryService.findById(id));
    }

    @GetMapping("/getByProduct/{id}")
    ResponseEntity<?> findByProductId(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(inventoryService.findByProductId(id));
    }

    @GetMapping("/getByLocation/{id}")
    ResponseEntity<?> findByLocationId(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(inventoryService.findByLocationId(id));
    }

    @GetMapping("/getByAddedBy/{id}")
    ResponseEntity<?> findByAddedById(@PathVariable("id") Long id) {
        return ResponseEntity.ofNullable(inventoryService.findByAddedById(id));
    }

    @GetMapping("/getExpiredNow")
    ResponseEntity<?> findByExpiredDate() {
        return ResponseEntity.ofNullable(inventoryService.findByExpiredDate());
    }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody InventoryDTO dto) {
        return ResponseEntity.ofNullable(inventoryService.update(dto));
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id) {
        inventoryService.delete(id);
        return ResponseEntity.ok().build();
    }


}

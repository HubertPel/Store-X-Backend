package pl.storex.storex.categories.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.storex.storex.categories.model.CategoryDTO;
import pl.storex.storex.categories.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
@Tag(name = "Categories Controller", description = "Controller to manipulate with categories table")
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.FOUND)
    ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @PostMapping("/getByAtribute")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.findByPassedAttribute(categoryDTO));
    }

    @PostMapping("/update")
    ResponseEntity<?> update(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(categoryDTO));
    }

    @DeleteMapping("/delete")
    ResponseEntity<?> delete(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.delete(categoryDTO));
    }


}

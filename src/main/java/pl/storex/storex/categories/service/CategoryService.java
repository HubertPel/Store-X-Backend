package pl.storex.storex.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.storex.storex.categories.model.Category;
import pl.storex.storex.categories.model.CategoryDTO;
import pl.storex.storex.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoriesRepository repository;

    public List<CategoryDTO> getAll() {
        List<CategoryDTO> categories = new ArrayList<>();
        repository.findAll().forEach(category -> {
            categories.add(CategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .created_at(category.getCreated_at())
                    .updated_at(category.getUpdated_at())
                    .updated_by(category.getUpdated_by())
                    .created_by(category.getCreated_by())
                    .deleted_by(category.getDeleted_by())
                    .deleted_at(category.getDeleted_at())
                    .build());
        });
        return categories;
    }

    public CategoryDTO findByPassedAttribute(CategoryDTO categoryDTO) {
        if (categoryDTO.getId() != null) {
            Optional<Category> byId = repository.findById(categoryDTO.getId());
            if (byId.isPresent()) {
                return byId.get().toModel();
            }
        }
        if (categoryDTO.getName() != null) {
            Optional<Category> byName = repository.findByName(categoryDTO.getName());
            if (byName.isPresent()) {
                return CategoryDTO.builder()
                        .name(byName.get().getName())
                        .id(byName.get().getId())
                        .created_at(byName.get().getCreated_at())
                        .updated_at(byName.get().getUpdated_at())
                        .updated_by(byName.get().getUpdated_by())
                        .created_by(byName.get().getCreated_by())
                        .deleted_by(byName.get().getDeleted_by())
                        .deleted_at(byName.get().getDeleted_at())
                        .build();
            }
        }
        return null;
    }

    public CategoryDTO update(CategoryDTO categoryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        if (categoryDTO.getId() == null) {
            return repository.save(Category.builder()
                    .id(categoryDTO.getId())
                    .name(categoryDTO.getName())
                    .created_by(loggedInUser.getId())
                    .updated_by(loggedInUser.getId())
                    .build()).toModel();
        }
        return repository.findById(categoryDTO.getId())
                .map(category -> {
                    category.setName(categoryDTO.getName());
                    category.setId(categoryDTO.getId());
                    category.setUpdated_by(loggedInUser.getId());
                    return repository.save(category).toModel();
                })
                .orElseThrow(() -> new RuntimeException("category not found"));

    }

    public String delete(CategoryDTO categoryDTO) {
       repository.delete(Category.fromModel(categoryDTO));
       return "deleted";
    }
}

package pl.storex.storex.categories.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.storex.storex.categories.model.Category;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);


}

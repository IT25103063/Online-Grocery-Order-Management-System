package com.groceryhub.dao;

import com.groceryhub.model.Category;
import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryDAO {

    private final TextFileDatabase db;
    private final String FILE_NAME = "categories";

    public List<Category> findAll() {
        return db.loadData(FILE_NAME, Category.class);
    }

    public Optional<Category> findById(Integer id) {
        return findAll().stream().filter(c -> c.getCategoryId().equals(id)).findFirst();
    }

    public Category save(Category category) {
        List<Category> categories = findAll();
        if (category.getCategoryId() == null) {
            category.setCategoryId(categories.size() > 0 ? categories.stream().mapToInt(Category::getCategoryId).max().orElse(0) + 1 : 1);
            categories.add(category);
        } else {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getCategoryId().equals(category.getCategoryId())) {
                    categories.set(i, category);
                    break;
                }
            }
        }
        db.saveData(FILE_NAME, categories);
        return category;
    }

    public void delete(Category category) {
        List<Category> categories = findAll();
        categories.removeIf(c -> c.getCategoryId().equals(category.getCategoryId()));
        db.saveData(FILE_NAME, categories);
    }
}

package ru.practicum.categories.categoriesadmin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        try {
            categoryRepository.save(category);
            log.info("Successfully added category: {}", category);
            return categoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Category name already in use.");
        }
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = getCategoryById(catId);
        category.setName(categoryDto.getName());
        try {
            categoryRepository.save(category);
            log.info("Successfully updated category: {}", category);
            return categoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Category name already in use.");
        }
    }

    public void deleteCategory(Long catId) {
        getCategoryById(catId);
        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
            log.info("Successfully deleted category with id: {}", catId);
        } else {
            throw new DataIntegrityViolationException("Cannot delete category with associated events.");
        }
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));
    }
}

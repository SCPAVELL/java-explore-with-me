package ru.practicum.categories.categoriespublic;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok().body(categoryPublicService.getCategories(from, size));
    }

    @GetMapping("{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) {
        return ResponseEntity.ok().body(categoryPublicService.getCategory(catId));
    }
}
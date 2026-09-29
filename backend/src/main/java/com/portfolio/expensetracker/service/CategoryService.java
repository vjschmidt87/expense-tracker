package com.portfolio.expensetracker.service;

import com.portfolio.expensetracker.dto.request.CategoryRequest;
import com.portfolio.expensetracker.dto.response.CategoryResponse;
import com.portfolio.expensetracker.entity.Category;
import com.portfolio.expensetracker.entity.User;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.exception.UnauthorizedException;
import com.portfolio.expensetracker.repository.CategoryRepository;
import com.portfolio.expensetracker.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryResponse> findAll(String username) {
        User user = findUser(username);
        return categoryRepository.findAllForUser(user.getId()).stream()
                .map(CategoryResponse::from).toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request, String username) {
        User user = findUser(username);
        Category cat = new Category();
        cat.setName(request.name());
        cat.setNamePt(request.namePt());
        cat.setIcon(request.icon());
        cat.setType(request.type());
        cat.setUser(user);
        return CategoryResponse.from(categoryRepository.save(cat));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request, String username) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (cat.getUser() == null) throw new UnauthorizedException("Cannot edit system categories");
        if (!cat.getUser().getUsername().equals(username)) throw new UnauthorizedException("Not your category");
        cat.setName(request.name());
        cat.setNamePt(request.namePt());
        cat.setIcon(request.icon());
        cat.setType(request.type());
        return CategoryResponse.from(categoryRepository.save(cat));
    }

    @Transactional
    public void delete(Long id, String username) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (cat.getUser() == null) throw new UnauthorizedException("Cannot delete system categories");
        if (!cat.getUser().getUsername().equals(username)) throw new UnauthorizedException("Not your category");
        categoryRepository.delete(cat);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}

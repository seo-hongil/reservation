package project.toy.reservation.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.toy.reservation.global.exception.BusinessException;
import project.toy.reservation.global.exception.ErrorCode;
import project.toy.reservation.store.dto.CategoryDto;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.repository.CategoryRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    public String root(){
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Category category = categoryRepository.findByName("식당").orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        List<CategoryDto> mainCategories = categoryRepository.findAllByParentId(category.getId())
                .stream()
                .map(CategoryDto::toCategoryDto)
                .toList();

        model.addAttribute("mainCategories", mainCategories);

        return "main";
    }

}

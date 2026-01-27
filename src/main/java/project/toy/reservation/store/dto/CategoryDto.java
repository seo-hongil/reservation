package project.toy.reservation.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.toy.reservation.store.entity.Category;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
        private Long id;
        private String name;
        private Long parentId;
        private String code;

    public static CategoryDto toCategoryDto(Category c) {
        return CategoryDto.builder()
                .id(c.getId())
                .name(c.getName())
                .code(c.getCode())
                .parentId(c.getParent() != null ? c.getParent().getId() : null)
                .build();
    }

}

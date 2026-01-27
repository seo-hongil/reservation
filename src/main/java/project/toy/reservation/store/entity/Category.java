package project.toy.reservation.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Builder.Default
    private Integer depth = 1;

    @Builder.Default
    private Integer priority = 0;

    // 연관관계 편의 메서드
    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }
}

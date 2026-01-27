package project.toy.reservation.store.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import project.toy.reservation.store.entity.Store;

import java.util.List;

import static project.toy.reservation.store.entity.QCategory.category;
import static project.toy.reservation.store.entity.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Store> searchStores(Long categoryId, String keyword) {
        return queryFactory
                .selectFrom(store)
                .join(store.category, category).fetchJoin()
                .where(
                        categoryEq(categoryId),
                        keywordContains(keyword)
                )
                .fetch();
    }

    // --- 동적 쿼리용 조립 메서드 (null이면 무시됨) ---

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword)
                ? store.name.contains(keyword).or(store.address.contains(keyword))
                : null;
    }
}

package project.toy.reservation.store;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.toy.reservation.store.dto.StoreRequest;
import project.toy.reservation.store.dto.StoreResponse;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.ReservationType;
import project.toy.reservation.store.entity.Store;
import project.toy.reservation.store.repository.CategoryRepository;
import project.toy.reservation.store.repository.StoreRepository;
import project.toy.reservation.store.service.StoreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @InjectMocks
    private StoreService storeService; // 테스트 대상

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상점 리스트 조회 시 비즈니스 로직이 맞게 가공되서 출력하는지 확인")
    void getSotres_whenkeyword_Test(){
        //given
        Category parentCategory = Category.builder().id(1L).name("식당").build();
        Category childCategory = Category.builder().id(2L).name("일식").parent(parentCategory).build();

        Store mockStore = Store.builder()
                .id(162L)
                .name("미식가")
                .address("서울 강남구 신사동 123-4")
                .addressDetail("1층")
                .thumbnailUrl("NOIMAGE")
                .category(childCategory)
                .reservationType(List.of(ReservationType.REMOTE,ReservationType.FIELD))
                .build();

        //when
        when(storeRepository.searchStores(any(),any())).thenReturn(List.of(mockStore));
        when(categoryRepository.findByName("식당")).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findAllByParentId(parentCategory.getId())).thenReturn(List.of());

        StoreRequest.SearchCond cond = new StoreRequest.SearchCond();
        cond.setKeyword("맛집");

        StoreResponse.StoreList result = storeService.getStores(cond);

        //then
        assertEquals("맛집",result.getKeyword());
        assertEquals(0,result.getCategories().size());
        assertNull(result.getSelectedCategoryId());

        StoreResponse.StoreInfo storeInfo = result.getStores().get(0);

        assertEquals("신사동", storeInfo.getAddress());
        assertEquals(2,storeInfo.getType().size());
    }

    @Test
    @DisplayName("특정 카테고리를 선택했을 때 상점 리스트와 카테고리 리스트가 모두 잘 가공되어야 한다")
    void getStores_selectedCategory_Test() {
        // given
        Long selectedId = 10L;
        Category parent = Category.builder().id(1L).name("식당").build();
        Category child = Category.builder().id(selectedId).name("일식").parent(parent).build();

        Store mockStore = Store.builder()
                .id(162L)
                .name("미식가")
                .address("서울 강남구 신사동 123-4")
                .category(child)
                .reservationType(List.of(ReservationType.REMOTE))
                .build();

        StoreRequest.SearchCond cond = new StoreRequest.SearchCond();
        cond.setCategoryId(selectedId);

        // when
        when(categoryRepository.findById(selectedId)).thenReturn(Optional.of(child));
        when(categoryRepository.findAllByParentId(parent.getId())).thenReturn(List.of(child));

        when(storeRepository.searchStores(any(), any())).thenReturn(List.of(mockStore));

        // 3. When
        StoreResponse.StoreList result = storeService.getStores(cond);

        // 4. Then
        assertEquals(1, result.getStores().size());
        assertEquals("신사동", result.getStores().get(0).getAddress());
        assertEquals(selectedId, result.getSelectedCategoryId());
    }
}

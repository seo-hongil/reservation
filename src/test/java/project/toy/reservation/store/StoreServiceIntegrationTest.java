package project.toy.reservation.store;

import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.entity.MemberStatus;
import project.toy.reservation.member.entity.RoleStatus;
import project.toy.reservation.member.repository.MemberRepository;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.Store;
import project.toy.reservation.store.repository.CategoryRepository;
import project.toy.reservation.store.repository.StoreRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StoreServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EntityManager em;

    private Member testmember;
    private Category testCategory;

    // 테스트 중 생성된 상점 ID를 저장하여 사후 정리에 사용
    private List<Long> createdStoreIds = new ArrayList<>();

    @Value("${file.uploadPath}")
    private String uploadPath;

    @BeforeEach
    void setUp() {
        testmember = memberRepository.save(Member.builder()
                .email("test@email.com")
                .name("홍길동")
                .password("1234")
                .phone("010-1234-5678")
                .role(RoleStatus.ADMIN)
                .status(MemberStatus.ACTIVE)
                .build());

        testCategory = categoryRepository.save(Category.builder()
                .name("한식")
                .depth(1)
                .priority(1)
                .build());

        createdStoreIds.clear();
    }

    /**
     * 테스트 종료 후 생성된 파일 및 폴더 삭제
     */
    @AfterEach
    void tearDown() throws IOException {
        for (Long storeId : createdStoreIds) {
            Path storeFolderPath = Paths.get(uploadPath, "store", String.valueOf(storeId));
            if (Files.exists(storeFolderPath)) {
                Files.walk(storeFolderPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                System.err.println("삭제 실패: " + path + " - " + e.getMessage());
                            }
                        });
            }
        }
    }

    @Test
    @DisplayName("상점 상품등록과 파일도 등록")
    @WithMockUser(username = "test@email.com")
    void success_register_store() throws Exception {
        MockMultipartFile imageFile1 = new MockMultipartFile(
                "imageUrls", "delicious1.png", "image/png", "test-data1".getBytes());
        MockMultipartFile imageFile2 = new MockMultipartFile(
                "imageUrls", "delicious2.png", "image/png", "test-data2".getBytes());
        MockMultipartFile imageFile3 = new MockMultipartFile(
                "imageUrls", "delicious3.png", "image/png", "test-data3".getBytes());

        String result = mockMvc.perform(multipart("/api/store/register")
                        .file(imageFile1)
                        .file(imageFile2)
                        .file(imageFile3)
                        .param("name", "백종원의 쌈밥")
                        .param("categoryId", String.valueOf(testCategory.getId()))
                        .param("address", "서울시 강남구")
                        .param("addressDetail", "1층")
                        .param("openTime", "09:00")
                        .param("closeTime", "21:00")
                        .param("reservationTypes", "REMOTE")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상점 등록이 완료되었습니다."))
                .andReturn().getResponse().getContentAsString();

        Long saveID = JsonPath.parse(result).read("$.data", Long.class);
        createdStoreIds.add(saveID); // 삭제 대상 리스트에 추가

        Store store = storeRepository.findById(saveID).orElseThrow();

        assertThat(store.getName()).isEqualTo("백종원의 쌈밥");
        assertThat(store.getImageUrls().size()).isEqualTo(3);

        for (String fileName : store.getImageUrls()) {
            Path path = Paths.get(uploadPath, "store", String.valueOf(store.getId()), fileName);
            // 파일이 실제로 생성되었는지 검증
            assertThat(Files.exists(path)).isTrue();
            assertThat(path.toString()).contains("/store/" + store.getId());
        }
        assertThat(store.getThumbnailUrl()).isEqualTo(store.getImageUrls().get(0));
    }

    @Test
    @DisplayName("이미지 없이 등록하면 썸네일이 noImage로 저장")
    @WithMockUser(username = "test@email.com")
    void register_without_image() throws Exception {
        String result = mockMvc.perform(multipart("/api/store/register")
                        .param("name", "백종원의 쌈밥2")
                        .param("categoryId", String.valueOf(testCategory.getId()))
                        .param("address", "서울시 강남구")
                        .param("addressDetail", "1층")
                        .param("openTime", "09:00")
                        .param("closeTime", "21:00")
                        .param("reservationTypes", "REMOTE")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long saveID = JsonPath.parse(result).read("$.data", Long.class);
        createdStoreIds.add(saveID);

        Store store = storeRepository.findById(saveID).orElseThrow();
        assertThat(store).isNotNull();
        assertThat(store.getThumbnailUrl()).isEqualTo("noImage");
        assertThat(store.getImageUrls()).isNullOrEmpty();
    }

    @Test
    @DisplayName("상점 정보 수정 - 이미지 유지, 추가, 삭제 통합 테스트")
    @WithMockUser(username = "test@email.com")
    void success_modify_store() throws Exception {
        MockMultipartFile initFile1 = new MockMultipartFile("imageUrls", "old1.png", "image/png", "old1".getBytes());
        MockMultipartFile initFile2 = new MockMultipartFile("imageUrls", "old2.png", "image/png", "old2".getBytes());

        String registerResult = mockMvc.perform(multipart("/api/store/register")
                        .file(initFile1).file(initFile2)
                        .param("name", "원본 상점")
                        .param("categoryId", String.valueOf(testCategory.getId())) // 등록 컨트롤러 파라미터명 확인
                        .param("address", "서울시")
                        .param("addressDetail", "본점") // 필수값 추가
                        .param("openTime", "09:00")
                        .param("closeTime", "21:00")
                        .param("reservationTypes", "REMOTE")
                        .with(csrf()))
                .andDo(print()) // 여기서 400 에러가 나는지 로그 확인 가능
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long storeId = JsonPath.parse(registerResult).read("$.data", Long.class);
        createdStoreIds.add(storeId);

        Store beforeStore = storeRepository.findById(storeId).orElseThrow();
        String keptImageName = beforeStore.getImageUrls().get(0);
        String deletedImageName = beforeStore.getImageUrls().get(1);

        // 2. 수정 실행
        MockMultipartFile newFile = new MockMultipartFile("images", "new.png", "image/png", "new-content".getBytes());

        mockMvc.perform(multipart("/api/store/modify")
                        .file(newFile)
                        .param("id", String.valueOf(storeId))
                        .param("parentCateId", String.valueOf(testCategory.getId())) // 필수값 추가
                        .param("childCateId", String.valueOf(testCategory.getId()))  // 필수값 추가
                        .param("name", "수정된 상점")
                        .param("address", "경기도")
                        .param("addressDetail", "수정분점") // 필수값 추가
                        .param("openTime", "10:00")
                        .param("closeTime", "22:00")
                        .param("reservationTypes", "REMOTE") // Enum 타입 확인 필요
                        .param("keepImages", keptImageName)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        em.flush();
        em.clear();

        // 3. 검증
        Store afterStore = storeRepository.findById(storeId).orElseThrow();
        assertThat(afterStore.getName()).isEqualTo("수정된 상점");
        assertThat(afterStore.getOpenTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(afterStore.getImageUrls()).hasSize(2);
        assertThat(afterStore.getImageUrls()).contains(keptImageName);
        assertThat(afterStore.getImageUrls()).doesNotContain(deletedImageName);

        // 물리 파일 검증
        assertThat(Files.exists(Paths.get(uploadPath, "store", String.valueOf(storeId), keptImageName))).isTrue();
        assertThat(Files.exists(Paths.get(uploadPath, "store", String.valueOf(storeId), deletedImageName))).isFalse();
    }
}
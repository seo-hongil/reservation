package project.toy.reservation.store.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.toy.reservation.common.file.FileService;
import project.toy.reservation.global.exception.BusinessException;
import project.toy.reservation.global.exception.ErrorCode;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.repository.MemberRepository;
import project.toy.reservation.store.dto.CategoryDto;
import project.toy.reservation.store.dto.StoreRequest;
import project.toy.reservation.store.dto.StoreRequest.ModifyRequest;
import project.toy.reservation.store.dto.StoreRequest.Register;
import project.toy.reservation.store.dto.StoreResponse;
import project.toy.reservation.store.dto.StoreResponse.StoreList;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.Store;
import project.toy.reservation.store.repository.CategoryRepository;
import project.toy.reservation.store.repository.StoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Transactional(readOnly = true)
    public StoreList getStores(StoreRequest.SearchCond cond) {
        List<CategoryDto> childrenCateList = new ArrayList<>();

        if (cond.getCategoryId() != null) {
            Category category = categoryRepository.findById(cond.getCategoryId()).orElseThrow(() -> new NoSuchElementException("카테고리가 존재하지 않습니다."));
            Long parentId = (category.getParent() != null) ? category.getParent().getId() : category.getId();
            childrenCateList = categoryRepository.findAllByParentId(parentId).stream().map(CategoryDto::toCategoryDto).toList();
        }else{
            Category parentCategory = categoryRepository.findByName("식당").orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
            childrenCateList = categoryRepository.findAllByParentId(parentCategory.getId()).stream().map(CategoryDto::toCategoryDto).toList();
        }

        List<StoreResponse.StoreInfo> storeInfos = storeRepository.searchStores(cond.getCategoryId(), cond.getKeyword()).stream().map(StoreResponse::toStoreInfoDto).toList();

        return StoreList.builder()
                .stores(storeInfos)
                .categories(childrenCateList)
                .selectedCategoryId(cond.getCategoryId())
                .keyword(cond.getKeyword())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryParent() {
        List<Category> categories = categoryRepository.findAllByDepth(1);

        if (categories.isEmpty()) {
            throw new EntityNotFoundException("대분류 카테고리가 등록되어 있지 않습니다.");
        }

        return categories.stream().map(CategoryDto::toCategoryDto).toList();
    }

    @Transactional
    public Long postRegisterStore(Register dto, List<MultipartFile> images, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Store store = StoreRequest.toNewStoreEntity(dto, member, category);
        Store saveStore = storeRepository.save(store);

        if (images != null && !images.isEmpty() && !images.get(0).isEmpty()) {
            List<String> uploadImages = fileService.uploadFiles(images, "store", saveStore.getId());

            saveStore.updateThumbnailFromImages(uploadImages.get(0));
            saveStore.addImages(uploadImages);
        } else {
            saveStore.updateThumbnailFromImages("noImage");
        }
        return saveStore.getId();
    }

    @Transactional
    public Long modifyStore(ModifyRequest dto, List<MultipartFile> images) {
        Category category = categoryRepository.findById(dto.getChildCateId()).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        Store store = storeRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchElementException("상점을 찾을 수 없습니다."));
        List<String> oldImages = new ArrayList<>(store.getImageUrls());

        store.modifyRequestDetails(dto, category);

        List<String> uploadedNames = fileService.uploadFiles(images, "store", store.getId());

        List<String> finalImages = new ArrayList<>(dto.getKeepImages());
        finalImages.addAll(uploadedNames);
        store.updateImages(finalImages);

        if (oldImages != null) {
            oldImages.stream()
                    .filter(name -> !dto.getKeepImages().contains(name))
                    .forEach(name -> fileService.deleteFile(name, "store", store.getId()));
        }

        return store.getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryChildren(Long parentId) {
        List<Category> categories = categoryRepository.findAllByParentId(parentId);
        return categories.stream().map(CategoryDto::toCategoryDto).toList();
    }

    @Transactional(readOnly = true)
    public ModifyRequest getStoreInfo(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NoSuchElementException("상점이 존재하지 않습니다."));
        return StoreRequest.toModifyDto(store);
    }
}

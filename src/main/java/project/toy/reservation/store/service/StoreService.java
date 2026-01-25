package project.toy.reservation.store.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.toy.reservation.common.file.FileService;
import project.toy.reservation.global.exception.BusinessException;
import project.toy.reservation.global.exception.ErrorCode;
import project.toy.reservation.member.entity.Member;
import project.toy.reservation.member.repository.MemberRepository;
import project.toy.reservation.store.dto.StoreRequest;
import project.toy.reservation.store.dto.StoreRequest.CategoryDto;
import project.toy.reservation.store.dto.StoreRequest.ModifyRequest;
import project.toy.reservation.store.dto.StoreRequest.Register;
import project.toy.reservation.store.dto.StoreResponse;
import project.toy.reservation.store.entity.Category;
import project.toy.reservation.store.entity.Store;
import project.toy.reservation.store.repository.CategoryRepository;
import project.toy.reservation.store.repository.StoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StoreService implements StoreProvider {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Override
    public StoreResponse getStores(String category, String keyword) {

        if(StringUtils.hasText(category)){
            // 카테고리만 넣어서 검색
        }

        if(StringUtils.hasText(keyword)){
            // 식당이름, 주소, 카테고리
        }

        return null;
    }

    public List<CategoryDto> getCategoryInfo() {
        List<Category> categories = categoryRepository.findAllByParentIsNullAndDepth(1L);

        if(categories.isEmpty()){
            throw new EntityNotFoundException("대분류 카테고리가 등록되어 있지 않습니다.");
        }

        return categories.stream().map(StoreRequest::toCategoryDto).toList();
    }

    @Transactional
    public Long postRegisterStore(Register dto, List<MultipartFile> images, String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new NoSuchElementException("사용자를 찾을 수 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Store store = StoreRequest.toNewStoreEntity(dto, member, category);
        Store saveStore = storeRepository.save(store);

        if (images != null && !images.isEmpty() && !images.get(0).isEmpty()) {
            List<String> uploadImages = fileService.uploadFiles(images, "store",saveStore.getId());

            saveStore.updateThumbnailFromImages(uploadImages.get(0));
            saveStore.addImages(uploadImages);
        }else{
            saveStore.updateThumbnailFromImages("noImage");
        }
        return saveStore.getId();
    }

    @Transactional
    public Long modifyStore(ModifyRequest dto, List<MultipartFile> images){
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

    public List<CategoryDto> getCategory(Long parentId){
        List<Category> categories = categoryRepository.findAllByParent_IdAndDepth(parentId, 2);
        return categories.stream().map(StoreRequest::toCategoryDto).toList();
    }

    public ModifyRequest getStoreInfo(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NoSuchElementException("상점이 존재하지 않습니다."));
        return StoreRequest.toModifyDto(store);
    }
}

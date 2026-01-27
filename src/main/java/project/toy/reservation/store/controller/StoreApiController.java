package project.toy.reservation.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.toy.reservation.global.common.ApiResponse;
import project.toy.reservation.store.dto.CategoryDto;
import project.toy.reservation.store.dto.StoreRequest.ModifyRequest;
import project.toy.reservation.store.dto.StoreRequest.Register;
import project.toy.reservation.store.service.StoreService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreApiController {

    private final StoreService storeService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Long>> registerStore(@Valid @ModelAttribute Register dto,
                                                     @RequestParam(value = "imageUrls", required = false)List<MultipartFile> images,
                                                     Principal principal){
        Long registerResult = storeService.postRegisterStore(dto, images, principal.getName());

        return ResponseEntity.ok(ApiResponse.success("상점 등록이 완료되었습니다.",registerResult));
    }

    @PostMapping("/modify")
    public ResponseEntity<ApiResponse<Long>> registerStore(@Valid @ModelAttribute ModifyRequest dto,
                                                           @RequestParam(value = "images", required = false)List<MultipartFile> images){
        Long registerResult = storeService.modifyStore(dto, images);

        return ResponseEntity.ok(ApiResponse.success("상점 수정이 완료되었습니다.",registerResult));
    }

    @GetMapping("/category/children")
    public ResponseEntity<List<CategoryDto>> getChildrenCategory(@RequestParam("parentId")Long parentId){
        List<CategoryDto> childCategory = storeService.getCategoryChildren(parentId);
        return ResponseEntity.ok(childCategory);
    }
}

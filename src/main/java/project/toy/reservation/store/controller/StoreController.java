package project.toy.reservation.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.toy.reservation.store.dto.StoreRequest.CategoryDto;
import project.toy.reservation.store.dto.StoreRequest.ModifyRequest;
import project.toy.reservation.store.dto.StoreRequest.Register;
import project.toy.reservation.store.service.StoreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/register")
    public String register(Model model){
        List<CategoryDto> registerStore = storeService.getCategoryInfo();
        model.addAttribute("parentCategories",  registerStore);
        model.addAttribute("registerRequest",  new Register());
        return "store/register";
    }

    @GetMapping("/manage")
    public String manage(@RequestParam(name = "storeId") Long storeId, Model model){
        ModifyRequest modifyRequest = storeService.getStoreInfo(storeId);
        List<CategoryDto> registerStore = storeService.getCategoryInfo();
        model.addAttribute("parentCategories",  registerStore);
        model.addAttribute("modifyRequest",  modifyRequest);
        return "store/modify";
    }
}

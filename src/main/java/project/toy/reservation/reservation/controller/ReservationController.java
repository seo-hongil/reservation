package project.toy.reservation.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.toy.reservation.store.service.StoreService;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final StoreService storeService;

    @GetMapping("/stores")
    public String getStores(@RequestParam(name="category") String category,
                           @RequestParam(name="keyword") String keyword,
                           Model model){
        model.addAttribute("data", storeService.getStores(category,keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "reservation/main";
    }

}

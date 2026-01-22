package project.toy.reservation.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.toy.reservation.member.dto.PasswordDto;
import project.toy.reservation.member.service.MemberService;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/verify-password")
    public ResponseEntity<Void> verifyPassword(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody PasswordDto.VerifyRequest request) {
        boolean isValid = memberService.verifyPassword(userDetails.getUsername(), request.getPassword());

        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody PasswordDto.ChangeRequest request) {
        memberService.updatePassword(userDetails.getUsername(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

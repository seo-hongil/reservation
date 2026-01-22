package project.toy.reservation.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class PasswordDto {

    @Getter
    @NoArgsConstructor
    public static class VerifyRequest {
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeRequest {
        private String newPassword;
    }
}

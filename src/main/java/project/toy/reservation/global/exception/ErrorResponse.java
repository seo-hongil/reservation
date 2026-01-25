package project.toy.reservation.global.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> errors;
}

package org.example.boardback.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    SUCCESS(HttpStatus.OK, "S000", "성공적으로 완료되었습니다.", "success"),
    CREATED(HttpStatus.CREATED, "S001", "데이터 생성이 성공적으로 완료되었습니다.","created successfully"),

    // ===========================
    // Common Errors (Cxxx)
    // ===========================
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다.", "Invalid input parameter"),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "C002", "잘못된 요청 타입입니다.", "Type mismatch"),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "C003", "JSON 형식이 올바르지 않습니다.", "JSON parse error"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C004", "지원하지 않는 HTTP 요청 방식입니다.", "HTTP method not supported"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "데이터를 찾을 수 없습니다.", "Entity not found"),
    DB_CONSTRAINT(HttpStatus.CONFLICT, "C006", "데이터 제약 조건 위반입니다.", "Database constraint violation"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C007", "서버 내부 오류가 발생했습니다.", "Internal server error"),

    // ===========================
    // Authentication / Token (Axxx)
    // ===========================
    INVALID_AUTH(HttpStatus.UNAUTHORIZED, "A001", "인증 정보가 올바르지 않습니다.", "Invalid credentials"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "A002", "인증에 실패했습니다.", "Authentication failed"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A003", "접근 권한이 없습니다.", "Access denied"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A004", "토큰이 만료되었습니다.", "Token expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "A005", "유효하지 않은 토큰입니다.", "Invalid token"),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "A006", "비밀번호와 비밀번호 확인이 일치하지 않습니다.", "Password mismatch"),

    // ===========================
    // User (Uxxx)
    // ===========================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다.", "User not found"),
    DUPLICATE_USER(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다.", "Duplicate user"),

    // ===========================
    // Payment (Pxxx)
    // ===========================
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "결제 내역을 찾을 수 없습니다.", "Payment not found"),
    PAYMENT_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "P002", "이미 처리된 결제입니다.", "Payment already processed"),
    PAYMENT_POINT_MISMATCH(HttpStatus.BAD_REQUEST, "P003", "결제 금액이 요청과 일치하지 않습니다.", "Payment amount mismatch"),
    PAYMENT_REFUND_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "P004", "해당 결제는 환불할 수 없습니다.", "Payment cannot be refunded"),
    PAYMENT_REFUND_AMOUNT_INVALID(HttpStatus.BAD_REQUEST, "P005", "환불 금액이 올바르지 않습니다.", "Invalid refund amount"),
    INSUFFICIENT_POINT_BALANCE(HttpStatus.BAD_REQUEST, "P006", "포인트 잔액이 부족합니다.", "Insufficient point balance"),
    PAYMENT_GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "P007", "결제 대행사와 통신 중 오류가 발생했습니다.", "Payment gateway error");


    private final HttpStatus status;
    private final String code;
    private final String message;     // client-friendly
    private final String logMessage;  // developer-friendly

    ErrorCode(HttpStatus status, String code, String message, String logMessage) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logMessage = logMessage;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (HTTP %d)", code, logMessage, status.value());
    }
}

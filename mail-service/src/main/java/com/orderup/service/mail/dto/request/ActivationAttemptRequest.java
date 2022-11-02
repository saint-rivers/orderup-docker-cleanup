package com.orderup.service.mail.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActivationAttemptRequest {
    private UUID userId;
    private Long otpCode;
    private LocalDateTime requestDate;
    private LocalDateTime expirationDate;
}

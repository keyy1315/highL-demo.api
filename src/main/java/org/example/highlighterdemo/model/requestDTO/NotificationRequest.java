package org.example.highlighterdemo.model.requestDTO;

import io.swagger.v3.oas.annotations.Operation;

public record NotificationRequest(
        @Operation(description = "comment / board / member")
        String referenceType,
        String referenceId,
        String commentId) {}

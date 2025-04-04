package org.example.highlighterdemo.model.requestDTO;

import io.swagger.v3.oas.annotations.Operation;
import org.example.highlighterdemo.model.entity.enums.NotificationAction;

public record NotificationRequest(
        NotificationAction action,
        @Operation(description = "comment / board / member")
        String referenceType,
        String referenceId,
        String commentId) {}

package org.wenant.domain.dto;

import org.wenant.enums.HealthStatus;

public record HealthResponseDto(HealthStatus status) {
}

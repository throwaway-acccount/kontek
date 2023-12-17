package se.kontek.task.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentPlanRequest(
    @NotNull @Positive Double principalAmount,
    @NotNull @Positive Integer amortizationPeriodYears
) {
}

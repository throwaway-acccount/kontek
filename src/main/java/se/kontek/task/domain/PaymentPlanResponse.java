package se.kontek.task.domain;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record PaymentPlanResponse(BigDecimal paidInterest, List<MonthlyPlan> monthlyPlans) {

    public record MonthlyPlan(YearMonth period, BigDecimal totalAmount, BigDecimal interestAmount, BigDecimal interestPercentage) {}
}

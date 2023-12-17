package se.kontek.task;

import org.springframework.stereotype.Service;
import se.kontek.task.domain.PaymentPlanResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

    private final LoanTypeRepository loanTypeRepository;

    public CalculatorService(final LoanTypeRepository loanTypeRepository) {
        this.loanTypeRepository = loanTypeRepository;
    }

    public PaymentPlanResponse calculatePaymentPlan(final String loanType, final Double principalAmount, final Integer amortizationPeriodYears) {
        final float interestRate = loanTypeRepository.findById(loanType)
            .map(loanTypeEntity -> loanTypeEntity.interest() / 100)
            .orElseThrow();

        double amountLeft = principalAmount;
        double paidInterest = 0.0;
        final var months = amortizationPeriodYears * 12;
        final var amortizationAmount = principalAmount / months;
        final var startPeriod = YearMonth.now();
        final var monthlyPlans = new ArrayList<PaymentPlanResponse.MonthlyPlan>();

        for (int i = 0; i < months; i++) {
            var monthlyInterest = Math.max(0, amountLeft * interestRate);
            var totalAmount = amortizationAmount + monthlyInterest;
            var interestPercentage = (monthlyInterest / totalAmount) * 100.0;

            var monthlyPlan = new PaymentPlanResponse.MonthlyPlan(
                startPeriod.plusMonths(i),
                BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_DOWN),
                BigDecimal.valueOf(monthlyInterest).setScale(2, RoundingMode.HALF_DOWN),
                BigDecimal.valueOf(interestPercentage).setScale(2, RoundingMode.HALF_DOWN)
            );

            monthlyPlans.add(monthlyPlan);
            amountLeft -= amortizationAmount;
            paidInterest += monthlyInterest;
        }

        return new PaymentPlanResponse(BigDecimal.valueOf(paidInterest).setScale(2, RoundingMode.HALF_DOWN), List.copyOf(monthlyPlans));
    }
}

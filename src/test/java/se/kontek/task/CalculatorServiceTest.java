package se.kontek.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.kontek.task.domain.LoanTypeEntity;
import se.kontek.task.domain.PaymentPlanResponse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    void setup() {
        when(loanTypeRepository.findById(any())).thenReturn(Optional.of(new LoanTypeEntity("", 1.0f)));
    }

    @Test
    void calculatesForOneYearCorrectly() {
        var paymentPlanResponse = calculatorService.calculatePaymentPlan("", 1200.0, 1);

        assertThat(paymentPlanResponse.monthlyPlans())
            .extracting(PaymentPlanResponse.MonthlyPlan::totalAmount)
            .extracting(BigDecimal::intValue)
            .containsExactly(112, 111, 110, 109, 108, 107, 106, 105, 104, 103, 102, 101);
    }

    @Test
    void calculatesFor10YearsCorrectly() {
        var paymentPlanResponse = calculatorService.calculatePaymentPlan("", 12000.0, 10);

        var expectedPayments = IntStream.rangeClosed(101, 220)
            .boxed()
            .sorted(Comparator.<Integer>naturalOrder().reversed())
            .toArray(Integer[]::new);

        assertThat(paymentPlanResponse.monthlyPlans())
            .extracting(PaymentPlanResponse.MonthlyPlan::totalAmount)
            .extracting(BigDecimal::intValue)
            .containsExactly(expectedPayments);
    }

    @Test
    void periodsAreSortedByAscendingOrder() {
        var paymentPlanResponse = calculatorService.calculatePaymentPlan("", 1200.0, 1);

        assertThat(paymentPlanResponse.monthlyPlans())
            .extracting(PaymentPlanResponse.MonthlyPlan::period)
            .isSortedAccordingTo(Comparator.naturalOrder());
    }

    @Test
    void zeroInterestRate() {
        when(loanTypeRepository.findById(any())).thenReturn(Optional.of(new LoanTypeEntity("", 0f)));

        var paymentPlanResponse = calculatorService.calculatePaymentPlan("", 1200.0, 1);

        assertThat(paymentPlanResponse.monthlyPlans())
            .allMatch(monthlyPlan -> monthlyPlan.totalAmount().intValue() == 100 && monthlyPlan.interestPercentage().doubleValue() == 0.0 && monthlyPlan.interestAmount().doubleValue() == 0.0);
    }

    @Test
    void responseIncludeRightAmountOfPeriods() {
        var years = 2;
        var expectedMonths = 12 * 2;
        var paymentPlanResponse = calculatorService.calculatePaymentPlan("", 1000.0, years);

        assertThat(paymentPlanResponse.monthlyPlans()).hasSize(expectedMonths);
    }
}
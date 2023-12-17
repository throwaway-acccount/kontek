package se.kontek.task;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.kontek.task.domain.PaymentPlanRequest;
import se.kontek.task.domain.PaymentPlanResponse;

@Validated
@RestController
@RequestMapping("calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(final CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("loan/{type}")
    public PaymentPlanResponse calculateLoan(@PathVariable String type, @Valid @RequestBody PaymentPlanRequest request) {
        return calculatorService.calculatePaymentPlan(type, request.principalAmount(), request.amortizationPeriodYears());
    }
}

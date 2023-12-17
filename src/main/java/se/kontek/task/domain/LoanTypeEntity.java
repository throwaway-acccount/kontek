package se.kontek.task.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("LOAN_TYPE")
public record LoanTypeEntity(@Id String type, Float interest) {
}

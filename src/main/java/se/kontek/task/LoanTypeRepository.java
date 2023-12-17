package se.kontek.task;

import org.springframework.data.repository.CrudRepository;
import se.kontek.task.domain.LoanTypeEntity;

public interface LoanTypeRepository extends CrudRepository<LoanTypeEntity, String> {
}

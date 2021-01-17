package splitter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpRepository extends CrudRepository<Account, Long> {

}

package mindhub.homebanking.repositories;

import mindhub.homebanking.models.Account;
import mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findById(long id);
    Account findByNumber(String number);
}

package mindhub.homebanking.repositories;

import mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {


    Card findById(long id);
    Card findByNumber(String number);
    Card findByCvv(long cvv);
}

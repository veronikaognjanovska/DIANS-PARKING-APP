package parkingapp.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import parkingapp.app.model.ConfirmationToken;

import java.util.Optional;

@Repository
public
interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

   public Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);

}
package parkingapp.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import parkingapp.app.model.ConfirmationToken;

@Repository
public
interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

    void saveConfirmationToken(ConfirmationToken confirmationToken);


}
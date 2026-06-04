package MITELOVERS.applicationservices;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


/**
 * Application service responsible for retrieving publication information
 * and converting domain objects into response DTOs.
 */

@Service
public class UserService {

    private final IUserRepo _iUserRepo;

    public UserService(IUserRepo userRepo) {

        _iUserRepo = userRepo;

    }

    public User getUserByEmail(String email) {

        return _iUserRepo.ofIdentity(new UserId(new Email(email)))

                .orElseThrow(() -> new NoSuchElementException("User not found: " + email));

    }

}

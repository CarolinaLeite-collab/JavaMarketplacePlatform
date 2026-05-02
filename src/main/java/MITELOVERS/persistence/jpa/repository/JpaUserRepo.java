package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.persistence.jpa.assembler.UserAssembler;
import MITELOVERS.persistence.jpa.datamodel.UserDataModel;
import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import MITELOVERS.persistence.springdata.IUserSpringDataRepo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation of {@link IUserRepo} for storing {@link User} instances.
 * <p>
 * Active only when the {@code jpa} Spring profile is enabled.
 * </p>
 */

@Repository
@Profile("jpa")
public class JpaUserRepo implements IUserRepo {

    private final IUserSpringDataRepo _iUserSpringDataRepo;
    private final UserAssembler _assembler;

    public JpaUserRepo(IUserSpringDataRepo iUserSpringDataRepo, UserAssembler assembler) {

        _iUserSpringDataRepo = iUserSpringDataRepo;
        _assembler = assembler;
    }

    @Override
    public User save(User user) {

        UserDataModel dataModel = _assembler.toDataModel(user);
        UserDataModel savedDM = _iUserSpringDataRepo.save(dataModel);

        return _assembler.toDomain(savedDM);
    }

    @Override
    public Iterable<User> findAll() {
        Iterable<UserDataModel> userDMs = _iUserSpringDataRepo.findAll();
        List<User> users = new ArrayList<>();
        for (UserDataModel userDM : userDMs) {
            users.add(_assembler.toDomain(userDM));
        }
        return users;
    }

    @Override
    public List<UserId> findAllKeys() {
        List<UserId> keys = new ArrayList<>();
        for (UserDataModel dm : _iUserSpringDataRepo.findAll()) {
            keys.add(new UserId(new Email(dm.getId())));
        }
        return keys;
    }

    @Override
    public Optional<User> ofIdentity(UserId userId) {
        UserDataModel savedDM = _iUserSpringDataRepo.findById(userId.toString())
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
        return Optional.of(_assembler.toDomain(savedDM));
    }

    @Override
    public boolean containsOfIdentity(UserId userId) {

        return _iUserSpringDataRepo.existsById(userId.toString());
    }

}


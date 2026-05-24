package MITELOVERS.controller.root;

import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationPolicy {

    /** Any authenticated user may browse genres. */
    public boolean canListGenres(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Only admins may add new genres. */
    public boolean canAddGenre(User user) {
        return user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may view their library. */
    public boolean canViewLibrary(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may create a private list. */
    public boolean canCreateList(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may put items for sale. */
    public boolean canSell(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }
}


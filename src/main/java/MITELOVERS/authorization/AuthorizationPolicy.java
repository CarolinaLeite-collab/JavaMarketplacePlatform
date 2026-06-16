package MITELOVERS.authorization;

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

    /** Any authenticated user may see a private list. */
    public boolean canSeeList(User user) {return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may add an item to a private list. */
    public boolean canAddItemTo(User user) {return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may delete a private list. */
    public boolean canDeleteList(User user) {return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may view public lists. */
    public boolean canSeePublicLists(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may consult a public list's items. */
    public boolean canSeeItemsInPublicList(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may put items for sale. */
    public boolean canSell(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Publication related authorization - can list Publication*/
    public boolean canListPublications(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Publication related authorization - can register Publication*/
    public boolean canCreatePublication(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Author related authorization - can list Authors */
    public boolean canListAuthors(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Author related authorization - can create Authors */
    public boolean canCreateAuthor(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may view their own library. */
    public boolean canGetLibrary(User user) {
        return user.hasRole(Role.USER);
    }

    /** Any authenticated user may add a publication to their library. */
    public boolean canAddToLibrary(User user) {
        return user.hasRole(Role.USER);
    }

    /** Only admins can register a Publishing Company*/
    public boolean canCreatePublishingCompany(User user) {
        return user.hasRole(Role.ADMIN);
    }

    /** Only admins can get a list of all Publishing Companies*/
    public boolean canGetAllPublishingCompanies(User user) {
        return user.hasRole(Role.ADMIN);
    }

    /** Only admins can search for a specific Publishing Company*/
    public boolean canGetPublishingCompany(User user) {
        return user.hasRole(Role.ADMIN);
    }

    /** Only admins may list expired or cancelled direct sales. */
    public boolean canListDirectSales(User user) {
        return user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may list active direct sales. */
    public boolean canListActiveDirectSales(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may create a direct sale. */
    public boolean canCreateDirectSale(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may filter direct sales by genre. */
    public boolean canFilterDirectSales(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may view a specific direct sale. */
    public boolean canGetDirectSale(User user) {
        return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);
    }

    /** Any authenticated user may register an item. */
    public boolean canCreateItem(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN); }

    /** Any authenticated user may list items. */
    public boolean canListItems(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN); }

    /** Any authenticated user may list publication types. */
    public boolean canListPublicationTypes(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Only admins may add a new publication type. */
    public boolean canCreatePublicationType(User user) { return user.hasRole(Role.ADMIN);}

    /** Any authenticated user may list editions. */
    public boolean canListEditions(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may create an edition. */
    public boolean canCreateEdition(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** A non registered user can´t see items' prices. */
    public boolean cannotSeePrice(User user) { return user.hasRole(Role.NONREGISTRED);}

    /** Any authenticated user may list countries. */
    public boolean canListCountries(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Any authenticated user may view a specific country. */
    public boolean canGetCountry(User user) { return user.hasRole(Role.USER) || user.hasRole(Role.ADMIN);}

    /** Only admins may create a country. */
    public boolean canCreateCountry(User user) { return user.hasRole(Role.ADMIN);}

}

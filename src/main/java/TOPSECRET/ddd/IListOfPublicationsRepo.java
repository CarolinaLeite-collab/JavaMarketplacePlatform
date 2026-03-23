package TOPSECRET.ddd;

import TOPSECRET.domain.Genre;
import TOPSECRET.domain.ListOfPublications;
import TOPSECRET.domain.User;

import java.util.List;

public interface IListOfPublicationsRepo {

    ListOfPublications addListOfPublications(User user, String name, Genre genre);

    List<ListOfPublications> getListOfListOfPublications();

    List<ListOfPublications> findPublicListsByGenre(Genre genre);

    List<ListOfPublications> findListsByUser(User user);

    ListOfPublications findByOwnerNameAndGenre(User user, String name, Genre genre);
}

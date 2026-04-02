package TOPSECRET.domain;

import TOPSECRET.domain.genre.Genre;

import java.util.List;

public interface IListOfPublicationsRepo {

    ListOfPublications addListOfPublications(User user, String name, Genre genre);

    List<ListOfPublications> getListOfListOfPublications();

    List<ListOfPublications> findPublicListsByGenre(Genre genre);

    List<ListOfPublications> findListsByUser(User user);

    ListOfPublications findByOwnerNameAndGenre(User user, String name, Genre genre);
}

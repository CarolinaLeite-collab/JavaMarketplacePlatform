package TOPSECRET.domain;

import TOPSECRET.domain.User.User;

import java.util.List;

public interface ILibraryRepo {

    Library addLibrary(User user);

    Library findLibraryByUser(User user);

    List<Item> getItemsInLibraryByUser(User user);
}

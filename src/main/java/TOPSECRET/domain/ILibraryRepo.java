package TOPSECRET.domain;

import TOPSECRET.domain.user.User;

import java.util.List;

public interface ILibraryRepo {

    Library addLibrary(User user);

    Library findLibraryByUser(User user);

    List<Item> getItemsInLibraryByUser(User user);
}

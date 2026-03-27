package TOPSECRET.domain;

import java.util.List;

public interface ILibraryRepo {

    Library addLibrary(User user);

    Library findLibraryByUser(User user);

    List<Item> getItemsInLibraryByUser(User user);
}

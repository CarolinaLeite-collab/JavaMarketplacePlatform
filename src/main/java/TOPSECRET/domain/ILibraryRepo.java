package TOPSECRET.domain;

import java.util.List;

public interface ILibraryRepo {

    public Library addLibrary(User user);

    public Library findLibraryByUser(User user);

    public List<Item> getItemsInLibraryByUser(User user);
}

package TOPSECRET.domain;

public class LibraryFactory {

    public Library createMyLibrary(User user) {
        return new Library(user);
    }
}

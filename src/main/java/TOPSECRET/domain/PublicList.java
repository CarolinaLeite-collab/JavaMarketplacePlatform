package TOPSECRET.domain;

public class PublicList {

    private final String _listName;
    private final String _ownerUsername;
    private final String _genre;
    private final boolean _isPublic;
    private final boolean _isPublished;

    public PublicList(String listName, String ownerUsername, String genre, boolean isPublic, boolean isPublished) {
        _listName = listName;
        _ownerUsername = ownerUsername;
        _genre = genre;
        _isPublic = isPublic;
        _isPublished = isPublished;
    }

    public String getListName() {
        return _listName;
    }

    public String getOwnerUsername() {
        return _ownerUsername;
    }

    public boolean isPublic() {
        return _isPublic;
    }

    public boolean isPublished() {
        return _isPublished;
    }

    public String getGenre() {
        return _genre;
    }


}
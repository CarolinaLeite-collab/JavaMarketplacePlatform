package TOPSECRET.domain;

public class PublicList {

    private final String listName;
    private final String ownerUsername;
    private final String genre;
    private final boolean isPublic;
    private final boolean isPublished;

    public PublicList(String listName, String ownerUsername, String genre, boolean isPublic, boolean isPublished) {
        this.listName = listName;
        this.ownerUsername = ownerUsername;
        this.genre = genre;
        this.isPublic = isPublic;
        this.isPublished = isPublished;
    }

    public String getListName() {
        return listName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public String getGenre() {
        return genre;
    }


}
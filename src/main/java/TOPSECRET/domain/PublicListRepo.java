package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class PublicListRepo {

    private final List<PublicList> lists = new ArrayList<>();

    public void add(PublicList list) {
        lists.add(list);
    }

    public List<PublicList> findPublicListsPublishedByGenre(String genre) {
        List<PublicList> result = new ArrayList<>();

        for (PublicList l : lists) {
            if (l.isPublic()
                    && l.isPublished()
                    && l.getGenre().equalsIgnoreCase(genre)) {
                result.add(l);
            }
        }

        return result;
    }
}
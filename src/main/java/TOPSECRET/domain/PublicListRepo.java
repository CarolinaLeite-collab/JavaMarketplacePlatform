package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class PublicListRepo {

    private final List<PublicList> _lists = new ArrayList<>();

    public void add(PublicList list) {
        _lists.add(list);
    }

    public List<PublicList> findPublicListsPublishedByGenre(String genre) {
        List<PublicList> result = new ArrayList<>();

        for (PublicList l : _lists) {
            if (l.isPublic()
                    && l.isPublished()
                    && l.getGenre().equalsIgnoreCase(genre)) {
                result.add(l);
            }
        }

        return result;
    }
}
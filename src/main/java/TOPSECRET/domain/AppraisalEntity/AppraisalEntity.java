package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.genre.Genre;
import TOPSECRET.domain.valueobject.Name;

import java.util.ArrayList;
import java.util.List;
/**
 * An {@link AppraisalEntity} is a registered {@link User} that is responsible for evaluating
 * the condition, authenticity, or value of a Publication before listing or sale, as defined
 * in the MiteLovers domain model.
 * <p>
 * Contains the appraisal entity's {@link Name}, the list of {@link PublicationType}s it can
 * appraise, and the list of {@link Genre}s it specializes in.
 *
 */

public class AppraisalEntity {
    private final Name _name;
    private final List<PublicationType> _publicationTypes;
    private final List<Genre> _genres;

    AppraisalEntity(Name name, List<PublicationType> publicationTypes, List<Genre> genres) {
        _name = name;
        _publicationTypes = new ArrayList<>(publicationTypes);
        _genres = new ArrayList<>(genres);
    }

    public Name getName() {
        return _name;
    }

    public List<Genre> getGenres() {
        return new ArrayList<>(_genres);
    }

    public List<PublicationType> getPublicationTypes() {
        return new ArrayList<>(_publicationTypes);
    }
}

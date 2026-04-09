package TOPSECRET.domain.appraisalEntity;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.user.User;
import TOPSECRET.domain.valueobject.AppraisalEntityId;
import TOPSECRET.domain.valueobject.GenreId;
import TOPSECRET.domain.valueobject.Name;
import TOPSECRET.domain.valueobject.PublicationTypeId;
import java.util.List;
import java.util.Objects;

/**
 * An {@link AppraisalEntity} is a registered {@link User} that is responsible for evaluating
 * the condition, authenticity, or value of a Publication before listing or sale, as defined
 * in the MiteLovers domain model.
 * <p>
 * Contains the appraisal entity's {@link Name}, the list of {@link PublicationTypeId}s it can
 * appraise, and the list of {@link GenreId}s it specializes in.
 *
 */

public class AppraisalEntity implements AggregateRoot<AppraisalEntityId> {

    private final Name _name;
    private final List<PublicationTypeId> _publicationTypesId;
    private final List<GenreId> _genresId;
    private final AppraisalEntityId _appraisalEntityId;


    AppraisalEntity(Name name, List<PublicationTypeId> publicationTypesId, List<GenreId> genresId) {

        if (publicationTypesId == null || publicationTypesId.isEmpty()) {
            throw new IllegalArgumentException("List of publication types cannot be null or empty");
        }
        if (genresId == null || genresId.isEmpty()) {
            throw new IllegalArgumentException("List of genres cannot be null or empty");
        }

        _name = name;
        _publicationTypesId = publicationTypesId;
        _genresId = genresId;
        _appraisalEntityId = new AppraisalEntityId(name.toString());

    }

    AppraisalEntity(AppraisalEntityId appraisalEntityId, Name name, List<PublicationTypeId> publicationTypesId, List<GenreId> genresId) {

        if (publicationTypesId == null || publicationTypesId.isEmpty()) {
            throw new IllegalArgumentException("List of publication types cannot be null or empty");
        }
        if (genresId == null || genresId.isEmpty()) {
            throw new IllegalArgumentException("List of genres cannot be null or empty");
        }

        _name = name;
        _publicationTypesId = publicationTypesId;
        _genresId = genresId;
        _appraisalEntityId = Objects.requireNonNull(appraisalEntityId, "AppraisalEntityId is required");

    }

    @Override
    public AppraisalEntityId identity() {

        return _appraisalEntityId;

    }

    @Override
    public boolean sameAs(Object object) {

        return equals(object);

    }

    public Name getName() {

        return _name;

    }

    public List<GenreId> getGenres() {

        return List.copyOf(_genresId);

    }

    public List<PublicationTypeId> getPublicationTypes() {

        return List.copyOf(_publicationTypesId);

    }

    @Override
    public boolean equals(Object object) {

        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof AppraisalEntity)) return false;
        AppraisalEntity appraisalEntity = (AppraisalEntity) object;
        return this._appraisalEntityId.equals(appraisalEntity._appraisalEntityId);

    }

}
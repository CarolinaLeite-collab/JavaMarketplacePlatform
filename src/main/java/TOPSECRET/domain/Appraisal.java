package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an appraisal of an object, including its estimated value, appraisal date, and description.
 * <p>
 * Ensures that the value estimate, appraisal date, and object description are not null or empty.
 * </p>
 */

public class Appraisal {
    private final Price _valueEstimate;
    private final LocalDateTime _appraisalDate;
    private final String _objectDescription;

    public Appraisal(Price valueEstimate, LocalDateTime appraisalDate, String objectDescription) {

        if (valueEstimate == null) {
            throw new IllegalArgumentException("Value estimate must not be null");
        }
        if (appraisalDate == null) {
            throw new IllegalArgumentException("Appraisal date must not be null");
        }
        if (objectDescription == null || objectDescription.isBlank()) {
            throw new IllegalArgumentException("Object description must not be empty");
        }

        _valueEstimate = valueEstimate;
        _appraisalDate = appraisalDate;
        _objectDescription = objectDescription;
    }

    public Price getValueEstimate() {
        return _valueEstimate;
    }

    public LocalDateTime getAppraisalDate() {
        return _appraisalDate;
    }

    public String getObjectDescription() {
        return _objectDescription;
    }

    //overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appraisal other)) return false;
        return _valueEstimate.equals(other.getValueEstimate())
                && _appraisalDate.equals(other.getAppraisalDate())
                && _objectDescription.equals(other.getObjectDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_valueEstimate, _appraisalDate, _objectDescription);
    }

    @Override
    public String toString() {
        return "Appraisal:" +
                " appraisal date=" + _appraisalDate +
                ", object description='" + _objectDescription + "'" +
                ", value estimate=" + _valueEstimate;
    }
}
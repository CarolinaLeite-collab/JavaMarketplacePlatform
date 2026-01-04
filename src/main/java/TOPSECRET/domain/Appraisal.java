package TOPSECRET.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Appraisal {
    private final Price _valueEstimate;
    private final LocalDateTime _appraisalDate;
    private final String _objectDescription;

    //constructor with validation on whether the fields are null/empty
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

        this._valueEstimate = valueEstimate;
        this._appraisalDate = appraisalDate;
        this._objectDescription = objectDescription;
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
        return _valueEstimate.equals(other._valueEstimate)
                && _appraisalDate.equals(other._appraisalDate)
                && _objectDescription.equals(other._objectDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_valueEstimate, _appraisalDate, _objectDescription);
    }

    @Override
    public String toString() {
        return "Appraisal:" +
                " appraisal date=" + _appraisalDate +
                ", object description='" + _objectDescription +
                ", value estimate=" + _valueEstimate;
    }
}
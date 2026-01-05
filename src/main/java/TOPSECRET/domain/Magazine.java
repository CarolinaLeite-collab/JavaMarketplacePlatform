package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a singular instance of a Magazine. This class encapsulates the state and behavior of a magazine,
 * including its publication information, physical condition, and a list of appraisals.
 * It enforces invariants such as non-null fields and provides controlled access
 * to its internal state to ensure encapsulation and immutability where applicable.
 * The list of appraisals is always returned as an immutable copy to prevent
 * external modifications.
 */

public class Magazine {

    private final PublicationInfo _publicationInfo;
    private Condition _condition;
    private final List<Appraisal> _appraisals;

    public Magazine(Condition _condition, PublicationInfo _publicationInfo) {

        // Throws exception if condition is null
        if (_condition == null) {
            throw new IllegalArgumentException("Condition cannot be null!");
        }

        // Throws exception if publication info is null
        if (_publicationInfo == null) {
            throw new IllegalArgumentException("Publication Info cannot be null!");
        }

        this._condition = _condition;
        this._publicationInfo = _publicationInfo;

        // Creates a new empty ArrayList of Appraisals
        this._appraisals = new ArrayList<>();
    }

    // Add an appraisal to the Appraisal list of Magazine
    public void addAppraisal(Appraisal appraisal) {

        if (appraisal == null) {
            throw new IllegalArgumentException("Appraisal cannot be null!");
        }

        _appraisals.add(appraisal);
    }

    // Show list of appraisals (copyOf to guarantee encapsulation)
    public List<Appraisal> getAppraisals() {
        return List.copyOf(_appraisals);
    }

    // Show condition (condition is an Enum, so no violation of encapsulation by using this method)
    public Condition getCondition() {
        return _condition;
    }

}

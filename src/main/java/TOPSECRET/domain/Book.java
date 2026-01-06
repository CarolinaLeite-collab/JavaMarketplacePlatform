package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Book {

    private final PublicationInfo _publicationInfo;
    private final Condition _condition;
    private final List<Appraisal> _appraisals;

    public Book(PublicationInfo publicationInfo, Condition condition) {

        if (publicationInfo == null) {
            throw new IllegalArgumentException("PublicationInfo cannot be null");
        }

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }

        _publicationInfo = publicationInfo;
        _condition = condition;
        _appraisals = new ArrayList<>();
    }

    public PublicationInfo getPublicationInfo() {
        return _publicationInfo;
    }

    public Condition getCondition() {
        return _condition;
    }

    public List<Appraisal> getAppraisals() {
        return Collections.unmodifiableList(_appraisals);
    }

    public void addAppraisal(Appraisal appraisal) {
        if (appraisal == null) {
            throw new IllegalArgumentException("Appraisal cannot be null");
        }
        _appraisals.add(appraisal);
    }
}

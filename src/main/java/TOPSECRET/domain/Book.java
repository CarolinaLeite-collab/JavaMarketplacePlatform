package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Book {

    private final PublicationInfo publicationInfo;
    private final Condition condition;
    private final List<Appraisal> appraisals;

    public Book(PublicationInfo publicationInfo, Condition condition) {

        if (publicationInfo == null) {
            throw new IllegalArgumentException("PublicationInfo cannot be null");
        }

        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }

        this.publicationInfo = publicationInfo;
        this.condition = condition;
        this.appraisals = new ArrayList<>();
    }
    
    public PublicationInfo getPublicationInfo() {
        return publicationInfo;
    }

    public Condition getCondition() {
        return condition;
    }
    
    public List<Appraisal> getAppraisals() {
        return Collections.unmodifiableList(appraisals);
    }
    
    public void addAppraisal(Appraisal appraisal) {
        if (appraisal == null) {
            throw new IllegalArgumentException("Appraisal cannot be null");
        }
        appraisals.add(appraisal);
    }
}

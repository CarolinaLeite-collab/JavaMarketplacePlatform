package TOPSECRET.domain;

public class ISSN {

    private final String _issn;

    public ISSN(String value) {
        if (value == null || !value.matches("\\d{4}-\\d{4}")){
            throw new IllegalArgumentException("Invalid ISSN format");
        }
        this._issn = value;
    }

    public String get_issn() {
        return _issn;
    }

    @Override
    public String toString() {
        return _issn;
    }
}

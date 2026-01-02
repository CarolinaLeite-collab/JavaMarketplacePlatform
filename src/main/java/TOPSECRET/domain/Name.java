package TOPSECRET.domain;

public final class Name {

    private final String _name;

    public Name(String _name) {
        if (_name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        String normalized = normalize(_name);

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        if (normalized.length() < 2 || normalized.length() > 80) {
            throw new IllegalArgumentException("Name must have between 2 and 80 characters");
        }

        // Letters (any language), spaces, hyphen and apostrophe.
        if (!normalized.matches("[\\p{L}]+([\\p{L}\\s'-]*[\\p{L}])?")) {
            throw new IllegalArgumentException("Name contains invalid characters");
        }
        this._name = normalized;
    }

    public String get_Name() {
        return _name;
    }

    @Override
    public String toString() {
        return _name;
    }

    private static String normalize(String raw) {
        // Trim + collapse multiple spaces into one
        return raw.trim().replaceAll("\\s+", " ");
    }
}

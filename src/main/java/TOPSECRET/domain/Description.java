package TOPSECRET.domain;

public class Description {

    public static final int MAX_LENGTH = 500;

    private String _description;

    public Description(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        if (description.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description too long (maximum of " + MAX_LENGTH + " characters)");
        }
        _description = description.trim();
    }
    // Mutability
    public void setDescription(String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        if (newDescription.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description too long (maximum of " + MAX_LENGTH + " characters)");
        }
        _description = newDescription.trim();
    }

    public String getDescription() {
        return _description;
    }
    public int getLength() {
        return _description.length();
    }

    // Adding a Maxlength boolean here may be useful later on (UI)

    @Override
    public String toString() {
        return _description + " (" + getLength() + "/" + Description.MAX_LENGTH + ")";
    }
}

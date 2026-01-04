package TOPSECRET.domain;

public class Description {

    public static final int MAX_LENGTH = 500;

    private String description;

    public Description(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        if (description.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description too long (maximum of " + MAX_LENGTH + " characters)");
        }
        this.description = description.trim();
    }
    // Mutability
    public void setDescription(String newDescription) {
        if (newDescription == null || newDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        if (newDescription.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Description too long (maximum of " + MAX_LENGTH + " characters)");
        }
        this.description = newDescription.trim();
    }

    public String getDescription() {
        return description;
    }
    public int getLength() {
        return description.length();
    }
    // Adding a Maxlength boolean here may be useful later on (UI)

    @Override
    public String toString() {
        return description + " (" + getLength() + "/" + Description.MAX_LENGTH + ")";
    }
}

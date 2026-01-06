package TOPSECRET.domain;

public class Title {

    /**
     * Title of a publication from PublicationInfo. Cannot be null, empty, or whitespace‑only.
     */

    private final String _title;

    public Title(String title) {

        if (!isValidConstructorArgument(title)) {
            throw new IllegalArgumentException("Condition cannot be null!");
        }

        this._title = title.trim();

    }

    public boolean isValidConstructorArgument(String title) {
        if (title == null || title.isBlank()) {
            return false;
        }
        return true;
    }

    public String getTitle() {
        return this._title; // Return original version, not lowercased version
    }

    // To standardize titles, as some entries may have varying capitalization
    public String getLowercaseTitle() {
        return this._title.toLowerCase();
    }

}

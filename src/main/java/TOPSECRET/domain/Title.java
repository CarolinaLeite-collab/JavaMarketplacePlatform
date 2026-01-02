package TOPSECRET.domain;

public class Title {

    /**
     * Title of a publication from PublicationInfo. Cannot be null, empty, or whitespace‑only.
     */

    private final String _title;

    public Title(String title) throws InstantiationException {
        if (!isValidConstructorArgument(title)) {
            throw new InstantiationException("Title cannot be null, empty, or blank");
        }

        this._title = title.trim();

    }

    private boolean isValidConstructorArgument(String title) {
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

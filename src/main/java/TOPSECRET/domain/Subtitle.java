package TOPSECRET.domain;

public class Subtitle extends Title {

    /**
     * Subtitle of a publication from PublicationInfo. Can be blank, unlike Title.
     */

    private final String _subtitle;

    public Subtitle(String subtitle) throws InstantiationException {
        // Pass a dummy non-blank value to satisfy parent constructor
        super(subtitle == null || subtitle.isBlank() ? "PLACEHOLDER" : subtitle);

        // Store the actual subtitle (which may be blank)
        this._subtitle = (subtitle == null) ? "" : subtitle.trim();
    }

    @Override
    public String getTitle() {
        return this._subtitle;
    }

    @Override
    public String getLowercaseTitle() {
        return this._subtitle.toLowerCase();
    }
}
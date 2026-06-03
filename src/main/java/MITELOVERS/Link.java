package MITELOVERS;

public class Link {

    private final String _rel;
    private final String _href;
    private final String _method; // usually GET

    public Link(String rel, String href) {
        this(rel, href, "GET");
    }

    public Link(String rel, String href, String method) {
        _rel = rel;
        _href = href;
        _method = method;
    }

    public String rel() { return _rel; }
    public String href() { return _href; }
    public String method() { return _method; }
}

package MITELOVERS;

public class Action {

    private final String _name;
    private final String _method;
    private final String _href;
    private final Object _schema;

    public Action(String name, String method, String href) {
        this(name, method, href, null);
    }

    public Action(String name, String method, String href, Object schema) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Action name must not be empty");
        }
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("HTTP method must not be empty");
        }
        if (href == null || href.isBlank()) {
            throw new IllegalArgumentException("href must not be empty");
        }

        _name = name;
        _method = method;
        _href = href;
        _schema = schema;
    }

    public String name() {
        return _name;
    }

    public String method() {
        return _method;
    }

    public String href() {
        return _href;
    }

    public Object schema() {
        return _schema;
    }
}

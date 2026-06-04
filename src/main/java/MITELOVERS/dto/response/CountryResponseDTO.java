package MITELOVERS.dto;

import MITELOVERS.Action;
import MITELOVERS.Link;
import lombok.Generated;

import java.util.ArrayList;
import java.util.List;

@Generated
public class CountryDTO {

    private final String _id;
    private final String _name;

    private final List<Link> links = new ArrayList<>();
    private final List<Action> actions = new ArrayList<>();

    public CountryDTO(String id, String name) {
        _id = id;
        _name = name;
    }

    public String id() {
        return _id;
    }

    public String name() {
        return _name;
    }

    public List<Link> links() {
        return List.copyOf(links);
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public List<Action> actions() {
        return List.copyOf(actions);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

}

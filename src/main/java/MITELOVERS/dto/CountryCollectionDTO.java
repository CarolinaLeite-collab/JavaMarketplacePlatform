package MITELOVERS.dto;

import MITELOVERS.Action;
import MITELOVERS.Link;
import lombok.Generated;

import java.util.ArrayList;
import java.util.List;

@Generated
public class CountryCollectionDTO {

    private final List<CountryDTO> _countries = new ArrayList<>();
    private final List<Link> _links = new ArrayList<>();
    private final List<Action> _actions = new ArrayList<>();

    public CountryCollectionDTO(List<CountryDTO> countries) {
        _countries.addAll(countries);
    }

    public List<CountryDTO> countries() {
        return List.copyOf(_countries);
    }

    public List<Link> links() {
        return List.copyOf(_links);
    }

    public void addLink(Link link) {
        _links.add(link);
    }

    public List<Action> actions() {
        return List.copyOf(_actions);
    }

    public void addAction(Action action) {
        _actions.add(action);
    }

}

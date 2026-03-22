package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;

import java.util.List;

public interface IItemRepo {

    public boolean exists(Publication publication);

    public Item createItem(Publication publication, Condition condition);

    public List<Item> getAll();

    public List<Item> getDifferentOf(List<Item> existentItems);

}

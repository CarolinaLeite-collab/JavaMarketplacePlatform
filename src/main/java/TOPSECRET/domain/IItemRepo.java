package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;

import java.util.List;

public interface IItemRepo {

    boolean exists(Publication publication);

    Item createItem(Publication publication, Condition condition);

    List<Item> getAll();

    List<Item> getDifferentOf(List<Item> existentItems);
}

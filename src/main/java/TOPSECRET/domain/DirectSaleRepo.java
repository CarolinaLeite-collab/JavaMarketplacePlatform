package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;

public class DirectSaleRepo {

    private List<DirectSale> _directSales;

    public DirectSaleRepo() {

        _directSales = new ArrayList<>();

    }

    public List<Item> getDirectSaleItemsByAuthor (Author author) {

        List<Item> listOfDirectSaleItemsByAuthor = new ArrayList<>();

        for (DirectSale directSale: _directSales) {

            if (directSale.isByAuthor(author)) {

                listOfDirectSaleItemsByAuthor.add(directSale.getItem());

            }


        }

        List<Item> copyOfListOfDirectSaleItemsByItem = List.copyOf(listOfDirectSaleItemsByAuthor);

        return copyOfListOfDirectSaleItemsByItem;

    }


}

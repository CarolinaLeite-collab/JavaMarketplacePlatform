package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.List;
import java.time.Period;

public class DirectSaleRepo {

    private List<DirectSale> _directSales;

    public DirectSaleRepo() {

        _directSales = new ArrayList<>();

    }

    public DirectSale createDirectSale(Item item, Price price, Period timeLimit) {

        DirectSale directSale = new DirectSale(item, price, timeLimit);

        _directSales.add(directSale);

        return directSale;

    }

    public List<Item> getDirectSaleItemsByAuthor (Author author) {

        List<Item> listOfDirectSaleItemsByAuthor = new ArrayList<>();

        for (DirectSale directSale: _directSales) {

            if (directSale.isByAuthor(author)) {

                listOfDirectSaleItemsByAuthor.add(directSale.getItem());

            }


        }

        List<Item> copyOfListOfDirectSaleItemsByAuthor = List.copyOf(listOfDirectSaleItemsByAuthor);

        return copyOfListOfDirectSaleItemsByAuthor;

    }


}

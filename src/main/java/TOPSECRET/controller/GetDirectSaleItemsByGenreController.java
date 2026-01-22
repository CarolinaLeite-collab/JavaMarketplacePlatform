package TOPSECRET.controller;

import TOPSECRET.domain.DirectSaleRepo;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Item;

import java.util.List;

public class GetDirectSaleItemsByGenreController {

    private DirectSaleRepo _directSaleRepo;

    public GetDirectSaleItemsByGenreController(DirectSaleRepo directSaleRepo) {
        _directSaleRepo = directSaleRepo;
    }

    public List<Item> getDirectSaleItemsByGenre(Genre genre) {
        return _directSaleRepo.getDirectSaleItemsByGenre(genre);
    }
}

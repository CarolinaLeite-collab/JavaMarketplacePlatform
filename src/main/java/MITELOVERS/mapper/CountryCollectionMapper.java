package MITELOVERS.mapper;


import MITELOVERS.Action;
import MITELOVERS.Link;
import MITELOVERS.dto.CountryCollectionDTO;
import MITELOVERS.dto.CountryDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryCollectionMapper {

    public CountryCollectionDTO toDTO(List<CountryDTO> countries) {

        CountryCollectionDTO dto = new CountryCollectionDTO(countries);

        dto.addLink(new Link("self", "/countries"));

//        // COLLECTION ACTIONS
//        dto.addAction(new Action(
//                "CreateCountry",
//                "POST",
//                "/countries",
//                CreateCountryCommand.schema()
//        ));

        return dto;
    }

}

package MITELOVERS.mapper;

import MITELOVERS.Link;
import MITELOVERS.domain.country.Country;
import MITELOVERS.dto.CountryDTO;
import org.springframework.stereotype.Component;


@Component
public class CountryMapper {

    // Maps a single Country to CountryDTO
    public CountryDTO toDTO(Country country) {

        CountryDTO dto = new CountryDTO(
                country.identity().toString(),
                country.name().toString()
        );

        // Links (Get)
        dto.addLink(new Link("self", "/countries/" + dto.id()));

//        // Actions
//        dto.addAction(new Action(
//                "updateCountry",
//                "PUT",
//                "/countries/" + dto.id(),
//                CountryUpdateCommand.schema()
//        ));
//
//        dto.addAction(new Action(
//                "deleteCountry",
//                "DELETE",
//                "/countries/" + dto.id()
//        ));

        return dto;
    }

}

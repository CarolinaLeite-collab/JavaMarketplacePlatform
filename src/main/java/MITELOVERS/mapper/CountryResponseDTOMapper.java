package MITELOVERS.mapper;

import MITELOVERS.domain.country.Country;
import MITELOVERS.dto.response.CountryResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles Country domain objects into CountryResponseDTO instances.
 */

@Component
public class CountryResponseDTOMapper implements RepresentationModelAssembler<Country, CountryResponseDTO> {

    @Override
    public CountryResponseDTO toModel(Country country) {

        return new CountryResponseDTO(
                country.identity().toString(),
                country.name().toString()
        );
    }
}

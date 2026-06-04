package MITELOVERS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Generated
@AllArgsConstructor
public class CountryResponseDTO extends RepresentationModel<CountryResponseDTO> {

    private final String countryId;
    private final String name;

}

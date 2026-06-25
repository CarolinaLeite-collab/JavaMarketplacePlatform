package MITELOVERS.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequestDTO {

    @NotBlank
    private String itemId;
}

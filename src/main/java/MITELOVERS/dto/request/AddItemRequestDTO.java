package MITELOVERS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequestDTO {

    @NotBlank
    private String itemId;
}

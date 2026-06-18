package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.persistence.jpa.datamodel.SaleDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler responsible for converting between {@link Sale} domain objects
 * and {@link SaleDataModel} persistence objects.
 * <p>
 * Delegates domain object creation to {@link MITELOVERS.domain.sale.SaleFactory}.
 * </p>
 */

@Component
@AllArgsConstructor
public class SaleAssembler {

}

package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.SaleId;
import MITELOVERS.domain.valueobject.UserId;

import java.util.List;

/**
 * Repository interface for managing persistence and retrieval of {@link Sale} aggregates.
 * <p>
 * Extends {@link IRepository} with {@link SaleId} as the identity type
 * and {@link Sale} as the aggregate root type.
 * </p>
 */

public interface ISaleRepo extends IRepository<SaleId, Sale> {

    List<Sale> findByUserId(UserId userId);

}

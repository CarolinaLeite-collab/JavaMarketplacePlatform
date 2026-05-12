package MITELOVERS.domain.repository;

import MITELOVERS.ddd.IRepository;
import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.PublicationId;

public interface IPublicationRepo extends IRepository<PublicationId, Publication> {

}

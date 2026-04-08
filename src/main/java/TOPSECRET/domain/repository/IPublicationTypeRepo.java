package TOPSECRET.domain.repository;

import TOPSECRET.domain.publicationtype.PublicationType;

import java.util.List;

public interface IPublicationTypeRepo {

    PublicationType addPublicationType(String publicationTypeName);

    List<PublicationType> getAll();
}

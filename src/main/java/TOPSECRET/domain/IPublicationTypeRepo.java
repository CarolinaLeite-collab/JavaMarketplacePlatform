package TOPSECRET.domain;

import TOPSECRET.domain.PublicationType.PublicationType;

import java.util.List;

public interface IPublicationTypeRepo {

    PublicationType addPublicationType(String publicationTypeName);

    List<PublicationType> getAll();
}

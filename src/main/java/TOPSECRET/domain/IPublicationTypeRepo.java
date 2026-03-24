package TOPSECRET.domain;

import java.util.List;

public interface IPublicationTypeRepo {
    PublicationType addPublicationType(String publicationTypeName);
    List<PublicationType> getAll();
}

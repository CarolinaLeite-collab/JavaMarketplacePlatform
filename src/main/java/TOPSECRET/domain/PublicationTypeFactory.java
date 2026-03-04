package TOPSECRET.domain;



public class PublicationTypeFactory {

    public PublicationType newPublicationType (String publicationTypeName) throws IllegalArgumentException {

            return new PublicationType(publicationTypeName);

    }

}

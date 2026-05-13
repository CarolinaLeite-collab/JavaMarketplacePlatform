package MITELOVERS.persistence.jpa.assembler;

import MITELOVERS.domain.edition.Edition;
import MITELOVERS.domain.edition.EditionFactory;
import MITELOVERS.domain.valueobject.*;
import MITELOVERS.persistence.jpa.datamodel.DimensionDataModel;
import MITELOVERS.persistence.jpa.datamodel.EditionDataModel;
import MITELOVERS.persistence.jpa.datamodel.PublicationIdDataModel;
import MITELOVERS.persistence.jpa.datamodel.WeightDataModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Year;

/**
 * Assembler responsible for converting between {@link Edition} domain objects
 * and {@link EditionDataModel} persistence objects.
 */

@Component
@AllArgsConstructor
public class EditionAssembler {

    private final EditionFactory _editionFactory;


    public EditionDataModel toDataModel(Edition edition) {
        String stringId = edition.identity().toString();
        String stringTypeId = edition.getPublicationTypeId().toString();
        String stringIdentifier = edition.getIdentifier().getIdentifier();
        String stringIdentifierType  = edition.getIdentifier().getClass().getSimpleName();

        PublicationIdDataModel publicationIdDataModel = new PublicationIdDataModel(
                edition.getPublicationId().getTitle().toString(),
                edition.getPublicationId().getAuthorId().toString(),
                edition.getPublicationId().getReleaseYear().getValue()
        );

        String stringPubCompanyId = edition.getPublishingCompanyId().toString();
        int intPubYear = edition.getPublishingYear().getValue();
        Language language = edition.getEditionLanguage();

        //optional fields
        DimensionDataModel dimensionDM = null;

        if(edition.getDimension() != null) {
            Dimension dimension = edition.getDimension();
            dimensionDM = new DimensionDataModel(dimension.getWidth(), dimension.getHeight(),
                    dimension.getThickness(), dimension.getUnit().toString());
        }

        WeightDataModel weightDM = null;

        if(edition.getWeight() != null) {
            Weight weight = edition.getWeight();
            weightDM = new WeightDataModel(weight.getValue(), weight.getWeightUnit().toString());
        }

        Integer integerNumberOfPages = null;

        if (edition.getNumberOfPages() != null) {
            integerNumberOfPages = edition.getNumberOfPages().getNumberOfPages();
        }

        Integer integerEditionNumber = null;

        if (edition.getEditionNumber() != null) {
            integerEditionNumber = edition.getEditionNumber().getValue();
        }

        Binding binding = null;

        if (edition.getBinding() != null) {
            binding = edition.getBinding();
        }


        EditionDataModel dataModelToSave = new EditionDataModel(stringId, stringTypeId, stringIdentifier,
                stringIdentifierType, publicationIdDataModel, stringPubCompanyId, intPubYear, language,
                dimensionDM, weightDM, integerNumberOfPages, integerEditionNumber, binding);

        return dataModelToSave;
    }

    public Edition toDomain(EditionDataModel editionDataModel) {

        EditionId id =  new EditionId(editionDataModel.getId());

        PublicationTypeId  publicationTypeId = new PublicationTypeId(editionDataModel.getTypeId());

        Title title = new Title(editionDataModel.getPublicationIdDm().getTitle());
        AuthorId authorId = new AuthorId(editionDataModel.getPublicationIdDm().getAuthorId());
        Year publicationYear = Year.of(editionDataModel.getPublicationIdDm().getReleaseYear());
        PublicationId publicationId = new PublicationId(title, authorId, publicationYear);

        PublishingCompanyId publishingCompanyId = new PublishingCompanyId(editionDataModel.getPublishingCompanyId());

        Year pubYear = Year.of(editionDataModel.getPublishingYear());

        Language language = editionDataModel.getEditionLanguage();

        Identifier identifier = null;
        switch (editionDataModel.getIdentifierType()) {
            case "ISBN":
                identifier = new ISBN(editionDataModel.getIdentifier());
                break;
            case "ISSN":
                identifier = new ISSN(editionDataModel.getIdentifier());
                break;
            case "NoIdentifier":
                identifier = new NoIdentifier();
                break;
            default:
                throw new IllegalArgumentException("Identifier type unknown " + editionDataModel.getIdentifierType());
        }

        //optional fields
        NumberOfPages numberOfPages = null;
        if (editionDataModel.getNumberOfPages() != null) {
            numberOfPages = new NumberOfPages(editionDataModel.getNumberOfPages());
        }

        EditionNumber editionNumber = null;
        if (editionDataModel.getEditionNumber() != null) {
            editionNumber = new EditionNumber(editionDataModel.getEditionNumber());
        }

        Binding binding = null;
        if (editionDataModel.getBinding() != null) {
            binding = editionDataModel.getBinding();
        }

        Dimension dimension = null;
        if (editionDataModel.getDimensionDm() != null) {
            DimensionDataModel dimensionDm = editionDataModel.getDimensionDm();

            dimension = new Dimension(dimensionDm.getWidth(), dimensionDm.getHeight(),
                    dimensionDm.getThickness(), DimensionUnit.fromString(dimensionDm.getUnit()));
        }

        Weight weight = null;
        if (editionDataModel.getWeightDm() != null) {
            WeightDataModel weightDm = editionDataModel.getWeightDm();

            weight = new Weight(weightDm.getValue(), Weight.WeightUnit.fromAbbreviation(weightDm.getWeightUnit()));
        }

        return _editionFactory.createEdition(id, publicationTypeId, identifier, publicationId, publishingCompanyId,
                pubYear, language, dimension, weight, numberOfPages, editionNumber, binding);

    }
}

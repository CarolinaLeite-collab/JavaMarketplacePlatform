package TOPSECRET.persistence.mem;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.edition.EditionFactory;
import TOPSECRET.domain.repository.IEditionRepo;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;
import java.util.*;

/**
 * Represents an in-memory repository of {@link Edition} entities.
 * <p>
 * This class provides management mechanisms for
 * {@link Edition} objects.
 * Each edition is uniquely identified by its {@link EditionId}.
 * <p>
 */

public class MemoEditionRepo implements IEditionRepo {

    private final Map<EditionId, Edition> DATA = new HashMap<EditionId, Edition>();
    private final EditionFactory _editionFactory;

    public MemoEditionRepo(EditionFactory editionFactory) {
        _editionFactory = editionFactory;
    }

    @Override
    public Edition save(Edition editionEntity) {

        DATA.put(editionEntity.identity(), editionEntity);
        return editionEntity;

    }

    @Override
    public Iterable<Edition> findAll() {

        return DATA.values();

    }

    @Override
    public Optional<Edition> ofIdentity(EditionId id) {

        if (!containsOfIdentity(id))
            return Optional.empty();
        else
            return Optional.of(DATA.get(id));
    }

    @Override
    public boolean containsOfIdentity(EditionId id) {

        return DATA.containsKey(id);

    }


    @Override
    public Edition addEdition(PublicationTypeId typeId,
                              Identifier identifier,
                              PublicationId publicationId,
                              PublishingCompanyId publishingCompanyId,
                              Year publishingYear,
                              Language editionLanguage,
                              Dimension dimension,
                              Weight weight,
                              NumberOfPages numberOfPages,
                              EditionNumber editionNumber,
                              Binding binding) {

        Edition edition = _editionFactory.createEdition(
                typeId,
                identifier,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                dimension,
                weight,
                numberOfPages,
                editionNumber,
                binding
        );

        for (Edition existingEdition : findAll()) {
            if (identifier != null && existingEdition.getIdentifier() != null) {
                if (existingEdition.getPublicationTypeId().equals(typeId) &&
                        existingEdition.getIdentifier().equals(identifier)) {
                    throw new IllegalStateException("An Edition with this identifier already exists!");
                }
            } else {
                if (existingEdition.sameAs(edition)) {
                    throw new IllegalStateException("Edition already exists!");
                }
            }
        }

        return save(edition);
    }

    public List<EditionId> findAllKeys() {

        return new ArrayList<>(DATA.keySet());

    }



}

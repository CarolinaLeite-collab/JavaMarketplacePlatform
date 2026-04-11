package TOPSECRET.persistence.mem;

import TOPSECRET.domain.edition.Edition;
import TOPSECRET.domain.edition.EditionFactory;
import TOPSECRET.domain.repository.IEditionRepo;
import TOPSECRET.domain.valueobject.*;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Edition addEditionBook(BookId bookId,
                                  PublicationId publicationId,
                                  PublishingCompanyId publishingCompanyId,
                                  Year publishingYear,
                                  Language editionLanguage,
                                  Dimension dimension,
                                  Weight weight,
                                  NumberOfPages numberOfPages,
                                  EditionNumber editionNumber,
                                  Binding binding
    ) {
        Edition editionBook = _editionFactory.createEditionBook(
                bookId,
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
        if (bookId instanceof ISBN && containsOfIdentity(bookId)) {
            throw new IllegalStateException("An Edition with this ISBN already exists!");
        }

        if (bookId instanceof NoIsbnBook) {
            for (Edition existingEdition : findAll()) {
                if (existingEdition.sameAs(editionBook)) {
                    throw new IllegalStateException("Edition already exists!");
                }
            }
        }

        return save(editionBook);
    }

    @Override
    public Edition addEditionMagazine(MagazineId magazineId,
                                      PublicationId publicationId,
                                      PublishingCompanyId publishingCompanyId,
                                      Year publishingYear,
                                      Language editionLanguage,
                                      Dimension dimension,
                                      Weight weight,
                                      IssueNumber issueNumber,
                                      Periodicity periodicity

    ) {
        Edition editionMagazine = _editionFactory.createEditionMagazine(
                magazineId,
                publicationId,
                publishingCompanyId,
                publishingYear,
                editionLanguage,
                dimension,
                weight,
                issueNumber,
                periodicity
        );

        if (magazineId instanceof ISSN && containsOfIdentity(magazineId)) {
            throw new IllegalStateException("An Edition with this ISSN already exists!");
        }

        if (magazineId instanceof NoIssnMagazine) {
            for (Edition existingEdition : findAll()) {
                if (existingEdition.sameAs(editionMagazine)) {
                    throw new IllegalStateException("Edition already exists!");
                }
            }
        }

        return save(editionMagazine);
    }
}

package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.publication.Publication;
import MITELOVERS.domain.valueobject.PublicationId;
import MITELOVERS.persistence.jpa.assembler.PublicationAssembler;
import MITELOVERS.persistence.jpa.datamodel.PublicationDataModel;
import MITELOVERS.persistence.springdata.IPublicationSpringdataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaPublicationRepoTest {
    @InjectMocks
    private JpaPublicationRepo _repo;

    @Mock
    private PublicationAssembler _publicationAssemblerDouble;

    @Mock
    private IPublicationSpringdataRepo _publicationSpringdataRepoDouble;

    @Mock
    private PublicationDataModel _publicationDataModelDouble;

    @Mock
    private Publication _publicationDouble;

    @Mock
    private PublicationId _publicationIdDouble;

    @Test
    void shouldCorrectlySaveAndReturnPublication() {
        //arrange
        when(_publicationAssemblerDouble.toDataModel(any(Publication.class))).thenReturn(_publicationDataModelDouble);
        when(_publicationSpringdataRepoDouble.save(any(PublicationDataModel.class))).thenReturn(_publicationDataModelDouble);
        when(_publicationAssemblerDouble.toDomain(any(PublicationDataModel.class))).thenReturn(_publicationDouble);

        //SUT

        //act
        Publication result = _repo.save(_publicationDouble);

        //assert
        assertNotNull(result);
        assertEquals(_publicationDouble, result);
    }
    @Test
    void shouldCorrectlyFindAllKeysAndReturnIds() {
        //arrange
        when(_publicationDataModelDouble.getTitle()).thenReturn("exampleTitle");
        when(_publicationDataModelDouble.getAuthorId()).thenReturn("exampleAuthorId");
        when(_publicationDataModelDouble.getReleaseYear()).thenReturn("2000");

        when(_publicationSpringdataRepoDouble.findAll()).thenReturn(List.of(_publicationDataModelDouble));

        //act
        Iterable<PublicationId> result = _repo.findAllKeys();

        //assert
        List<PublicationId> ids = new ArrayList<>();
        for (PublicationId publicationId : result) {
            ids.add(publicationId);
        }

        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertEquals("exampleTitle-exampleAuthorId(2000)", ids.get(0).toString());
    }

    @Test
    void ofIdentityShouldCorrectlyReturnPublication() {
        //arrange
        when(_publicationIdDouble.toString()).thenReturn("exampleId");
        when(_publicationAssemblerDouble.toDomain(_publicationDataModelDouble)).thenReturn(_publicationDouble);
        when(_publicationSpringdataRepoDouble.findById(_publicationIdDouble.toString())).thenReturn(Optional.of(_publicationDataModelDouble));

        //SUT

        //act
        Optional<Publication> result = _repo.ofIdentity(_publicationIdDouble);

        //assert
        assertFalse(result.isEmpty());
        assertEquals(_publicationDouble, result.get());

    }

    @Test
    void containsOfIdentityShouldReturnTrueWhenContainsPublicationId() {
        //arrange
        when(_publicationIdDouble.toString()).thenReturn("exampleId");
        when(_publicationSpringdataRepoDouble.existsById(_publicationIdDouble.toString())).thenReturn(true);

        //act
        boolean result = _repo.containsOfIdentity(_publicationIdDouble);

        //assert
        assertTrue(result);
    }

    @Test
    void shouldCorrectlyReturnAllPublications() {
        //arrange
        when(_publicationSpringdataRepoDouble.findAll()).thenReturn(List.of(_publicationDataModelDouble));
        when(_publicationAssemblerDouble.toDomain(_publicationDataModelDouble)).thenReturn(_publicationDouble);

        //act
        Iterable<Publication> result = _repo.findAll();
        List<Publication> resultList = new ArrayList<>();

        for (Publication publication : result) {

            resultList.add(publication);

        }

        //assert
        assertEquals(1, resultList.size());
        assertEquals(_publicationDouble, resultList.get(0));
    }
}
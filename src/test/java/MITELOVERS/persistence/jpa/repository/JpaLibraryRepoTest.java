package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.library.Library;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.LibraryId;
import MITELOVERS.persistence.jpa.assembler.LibraryAssembler;
import MITELOVERS.persistence.jpa.datamodel.LibraryDataModel;
import MITELOVERS.persistence.springdata.ILibrarySpringDataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JpaLibraryRepoTest {

    @Mock
    private ILibrarySpringDataRepo springRepo;

    @Mock
    private LibraryAssembler assembler;

    @InjectMocks
    private JpaLibraryRepo repo;

    private LibraryId libraryId;
    private Library library;
    private LibraryDataModel dataModel;

    @BeforeEach
    void setup() {
        libraryId = new LibraryId(new Email("test@example.com"));
        library = Mockito.mock(Library.class);
        dataModel = Mockito.mock(LibraryDataModel.class);

        when(library.identity()).thenReturn(libraryId);
        when(dataModel.getLibraryId()).thenReturn("test@example.com");
    }

    // -----
    // save
    // -----
    @Test
    void save_ShouldConvertToDataModelPersistAndConvertBack() {

        when(assembler.toDataModel(library)).thenReturn(dataModel);
        when(springRepo.save(dataModel)).thenReturn(dataModel);
        when(assembler.toDomain(dataModel)).thenReturn(library);

        Library result = repo.save(library);

        assertEquals(library, result);
    }

    // ------------
    // findAllKeys
    // ------------
    @Test
    void findAllKeys_ShouldReturnAllLibraryIds() {

        when(springRepo.findAll()).thenReturn(List.of(dataModel));

        Iterable<LibraryId> result = repo.findAllKeys();

        List<LibraryId> list = (List<LibraryId>) result;
        assertEquals(1, list.size());
        assertEquals("test@example.com", list.get(0).toString());
    }

    // --------
    // findAll
    // --------
    @Test
    void findAll_ShouldReturnAllLibraries() {

        when(springRepo.findAll()).thenReturn(List.of(dataModel));
        when(assembler.listToDomain(List.of(dataModel))).thenReturn(List.of(library));

        Iterable<Library> result = repo.findAll();

        List<Library> list = (List<Library>) result;
        assertEquals(1, list.size());
        assertEquals(library, list.get(0));
    }

    // -------------
    // ofIdentity
    // -------------
    @Test
    void ofIdentity_ShouldReturnLibraryWhenFound() {

        when(springRepo.findById("test@example.com")).thenReturn(Optional.of(dataModel));
        when(assembler.toDomain(dataModel)).thenReturn(library);

        Optional<Library> result = repo.ofIdentity(libraryId);

        assertTrue(result.isPresent());
        assertEquals(library, result.get());
    }

    @Test
    void ofIdentity_ShouldReturnEmptyWhenNotFound() {

        when(springRepo.findById("test@example.com")).thenReturn(Optional.empty());

        Optional<Library> result = repo.ofIdentity(libraryId);

        assertTrue(result.isEmpty());
    }

    // -------------------
    // containsOfIdentity
    // -------------------
    @Test
    void containsOfIdentity_ShouldReturnTrueWhenExists() {

        when(springRepo.existsById("test@example.com")).thenReturn(true);

        assertTrue(repo.containsOfIdentity(libraryId));
    }

    @Test
    void containsOfIdentity_ShouldReturnFalseWhenNotExists() {

        when(springRepo.existsById("test@example.com")).thenReturn(false);

        assertFalse(repo.containsOfIdentity(libraryId));
    }

}
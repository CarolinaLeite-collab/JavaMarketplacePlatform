package TOPSECRET.controller;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetItemsOnDirectSaleOfAGivenPublishingCompanyControllerTest {

    private Item _item;
    private PublishingCompany _publisher;
    private GetItemsOnDirectSaleOfAGivenPublishingCompanyController _sut;

    @Mock
    private DirectSaleRepo _directSaleRepo;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        _publisher = new PublishingCompany("Penguin");
        Publication publication = Publication.builder()
                .type(new PublicationType("BOOK"))
                .identifier(new ISBN("9780691181950"))
                .year(Year.of(2019))
                .title(new Title("How to Keep Your Cool"))
                .author(new Author("Seneca"))
                .publisher(_publisher)
                .build();
        _item = new Item(publication, Condition.GOOD);
        // SUT
        _sut = new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(_directSaleRepo);
    }

    @Test
    void constructorThrowsNullPointerExceptionWhenDirectSaleRepoIsNull() {

        assertThrows(NullPointerException.class, () -> new GetItemsOnDirectSaleOfAGivenPublishingCompanyController(null));
    }

    @Test
    void getDirectSaleItemByPublisherThrowsIllegalArgumentExceptionWhenPublisherIsNull() {

        assertThrows(IllegalArgumentException.class, () -> _sut.getDirectSaleItemByPublisher(null));
    }

    @Test
    void getDirectSaleItemByPublisherDelegatesToRepository() {

        List<Item> expected = List.of(_item);
        when(_directSaleRepo.getDirectSaleItemsByPublisher(_publisher)).thenReturn(expected);

        List<Item> actual = _sut.getDirectSaleItemByPublisher(_publisher);

        verify(_directSaleRepo).getDirectSaleItemsByPublisher(_publisher);
        assertSame(expected, actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    void getDirectSaleItemByPublisherReturnsEmptyListWhenRepositoryReturnsEmpty() {

        List<Item> expected = List.of();
        when(_directSaleRepo.getDirectSaleItemsByPublisher(_publisher)).thenReturn(expected);

        List<Item> actual = _sut.getDirectSaleItemByPublisher(_publisher);

        verify(_directSaleRepo).getDirectSaleItemsByPublisher(_publisher);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        assertEquals(expected, actual);
    }
}
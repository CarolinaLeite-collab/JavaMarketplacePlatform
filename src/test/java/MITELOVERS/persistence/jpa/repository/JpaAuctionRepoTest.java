package MITELOVERS.persistence.jpa.repository;

import MITELOVERS.domain.auction.Auction;
import MITELOVERS.domain.valueobject.AuctionId;
import MITELOVERS.domain.valueobject.ItemId;
import MITELOVERS.persistence.jpa.assembler.AuctionAssembler;
import MITELOVERS.persistence.jpa.datamodel.AuctionDataModel;
import MITELOVERS.persistence.springdata.IAuctionSpringDataRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAuctionRepoTest {

    @Mock
    private IAuctionSpringDataRepo springDataRepo;

    @Mock
    private AuctionAssembler assemblerDouble;

    @InjectMocks
    private JpaAuctionRepo jpaAuctionRepo;

    @Test
    void testSaveReturnAuction() {
        //Arrange
        Auction auctionDouble = mock(Auction.class);
        AuctionDataModel dmDouble = mock(AuctionDataModel.class);
        AuctionDataModel savedDouble = mock(AuctionDataModel.class);

        when(assemblerDouble.toDataModel(auctionDouble)).thenReturn(dmDouble);
        when(springDataRepo.save(dmDouble)).thenReturn(savedDouble);
        when(assemblerDouble.toDomain(savedDouble)).thenReturn(auctionDouble);

        //Act
        Auction result = jpaAuctionRepo.save(auctionDouble);

        //Assert
        assertEquals(auctionDouble, result);
        assertNotNull(result);
    }

    @Test
    void testFindAllReturnIterableOfAuctions() {

        AuctionDataModel dm1Double = mock(AuctionDataModel.class);
        AuctionDataModel dm2Double = mock(AuctionDataModel.class);
        List<AuctionDataModel> dmList = List.of(dm1Double, dm2Double);

        Auction auction1Double = mock(Auction.class);
        Auction auction2Double = mock(Auction.class);
        List<Auction> auctionList = List.of(auction1Double, auction2Double);

        when(springDataRepo.findAll()).thenReturn(dmList);
        when(assemblerDouble.toDomain(dm1Double)).thenReturn(auction1Double);
        when(assemblerDouble.toDomain(dm2Double)).thenReturn(auction2Double);

        Iterable<Auction> result = jpaAuctionRepo.findAll();

        assertEquals(auctionList, result);
        assertNotNull(result);
    }

    @Test
    void testFindAllKeysReturnIds() {

        AuctionDataModel dm1Double = mock(AuctionDataModel.class);
        AuctionDataModel dm2Double = mock(AuctionDataModel.class);
        List<AuctionDataModel> dmList = List.of(dm1Double, dm2Double);

        when(dm1Double.getAuctionId()).thenReturn("AU-1234ABCD");
        when(dm2Double.getAuctionId()).thenReturn("AU-5678EFGH");
        when(springDataRepo.findAll()).thenReturn(dmList);

        List<AuctionId> result = jpaAuctionRepo.findAllKeys();

        assertEquals(2, result.size());
        assertEquals("AU-1234ABCD", result.get(0).toString());
        assertEquals("AU-5678EFGH", result.get(1).toString());
        assertNotNull(result);
    }

    @Test
    void testOfIdentityReturnsAuction() {
        //Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);
        AuctionDataModel dmDouble = mock(AuctionDataModel.class);
        Auction auctionDouble = mock(Auction.class);

        when(auctionIdDouble.toString()).thenReturn("AU-1234ABCD");
        when(springDataRepo.findById("AU-1234ABCD")).thenReturn(Optional.of(dmDouble));
        when(assemblerDouble.toDomain(dmDouble)).thenReturn(auctionDouble);

        //Act
        Optional<Auction> result = jpaAuctionRepo.ofIdentity(auctionIdDouble);

        //Assert
        assertTrue(result.isPresent());
        assertEquals(auctionDouble, result.get());
    }

    @Test
    void testContainsOfIdentityReturnsTrueWhenAuctionExists() {
        //Arrange
        AuctionId auctionIdDouble = mock(AuctionId.class);

        when(auctionIdDouble.toString()).thenReturn("AU-1234ABCD");
        when(springDataRepo.existsById("AU-1234ABCD")).thenReturn(true);

        //Act
        boolean result = jpaAuctionRepo.containsOfIdentity(auctionIdDouble);

        //Assert
        assertTrue(result);
    }

    @Test
    void findByItemsIdSortedShouldDelegateToSpringRepo() {

        ItemId item1 = mock(ItemId.class);
        ItemId item2 = mock(ItemId.class);

        when(item1.toString()).thenReturn("i1");
        when(item2.toString()).thenReturn("i2");

        AuctionDataModel dm = mock(AuctionDataModel.class);
        Auction auction = mock(Auction.class);

        when(springDataRepo.findAllByItemsIdOrderByAuctionEndDateAsc(List.of("i1", "i2")))
                .thenReturn(List.of(dm));

        when(assemblerDouble.toDomain(dm)).thenReturn(auction);
        when(auction.getItemsId()).thenReturn(List.of(item1, item2));

        List<ItemId> result = jpaAuctionRepo.findByItemsIdSorted(List.of(item1, item2));

        assertEquals(List.of(item1, item2), result);
    }

    @Test
    void findByItemsIdSortedShouldReturnEmptyListWhenNoAuctionsMatch() {

        ItemId item1 = mock(ItemId.class);
        when(item1.toString()).thenReturn("i1");

        when(springDataRepo.findAllByItemsIdOrderByAuctionEndDateAsc(List.of("i1")))
                .thenReturn(List.of());

        List<ItemId> result = jpaAuctionRepo.findByItemsIdSorted(List.of(item1));

        assertTrue(result.isEmpty());
    }

}
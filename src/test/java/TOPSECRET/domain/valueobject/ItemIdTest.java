package TOPSECRET.domain.valueobject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


    class ItemIdTest {

        private SKU _skuDouble;

        @BeforeEach
        void setUp() {
            _skuDouble = mock(SKU.class);
        }

        @Test
        void testAConstructor() {

            // SUT
            new ItemId(_skuDouble);
        }

        @Test
        void constructorWithNullSkuThrowsNullPointerException() {

            // act + assert
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new ItemId(null)
            );

            assertEquals("SKU is required.", exception.getMessage());
        }

        @Test
        void testGenerateCreatesValidItemId() {

            // act
            ItemId result = ItemId.generate();

            // assert
            assertNotNull(result);
            assertNotNull(result.getSku());
        }

        @Test
        void testGetSkuReturnsSameSku() {

            // SUT
            ItemId itemId = new ItemId(_skuDouble);

            // act
            SKU result = itemId.getSku();

            // assert
            assertEquals(_skuDouble, result);
        }

        @Test
        void itemIdIsEqualWithItself() {

            // SUT
            ItemId itemId = new ItemId(_skuDouble);

            // assert
            assertEquals(itemId, itemId);
        }

        @Test
        void itemIdIsNotEqualWithNull() {

            // SUT
            ItemId itemId = new ItemId(_skuDouble);

            // assert
            assertNotEquals(null, itemId);
        }

        @Test
        void itemIdIsNotEqualWithDifferentObjectType() {

            // arrange
            String differentType = "differentType";

            // SUT
            ItemId itemId = new ItemId(_skuDouble);

            // assert
            assertFalse(itemId.equals(differentType));
        }

        @Test
        void itemIdIsEqualWithAnotherItemIdWithSameSku() {

            // SUT
            ItemId itemId = new ItemId(_skuDouble);
            ItemId result = new ItemId(_skuDouble);

            // assert
            assertEquals(itemId, result);
        }

        @Test
        void itemIdIsNotEqualWithAnotherItemIdWithDifferentSku() {

            // arrange
            SKU sku2Double = mock(SKU.class);

            // SUT
            ItemId itemId = new ItemId(_skuDouble);
            ItemId result = new ItemId(sku2Double);

            // assert
            assertNotEquals(itemId, result);
        }

        @Test
        void hashIsEqualWithSameSku() {

            // SUT
            ItemId itemId = new ItemId(_skuDouble);
            ItemId result = new ItemId(_skuDouble);

            // assert
            assertEquals(itemId.hashCode(), result.hashCode());
        }

        @Test
        void hashIsNotEqualWithDifferentSkus() {

            // arrange
            SKU sku2Double = mock(SKU.class);

            // SUT
            ItemId itemId = new ItemId(_skuDouble);
            ItemId result = new ItemId(sku2Double);

            // assert
            assertNotEquals(itemId.hashCode(), result.hashCode());
        }

        @Test
        void testToString() {

            // arrange
            when(_skuDouble.toString()).thenReturn("SKU-123");

            // SUT
            ItemId itemId = new ItemId(_skuDouble);

            // act
            String result = itemId.toString();

            // assert
            assertEquals("SKU-123", result);
        }
    }

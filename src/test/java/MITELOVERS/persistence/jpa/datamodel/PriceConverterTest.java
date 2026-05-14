package MITELOVERS.persistence.jpa.datamodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceConverterTest {

    @Test
    void shouldSuccessfullyConvertToDatabaseColum(){
        //Arrange
        PriceDataModel price = new PriceDataModel(10.5, "EUR");
        String expected = "10.5_EUR";

        //SUT
        PriceConverter converter = new PriceConverter();

        //Act
        String converted = converter.convertToDatabaseColumn(price);

        //Assert
        assertEquals(expected,converted);
    }

    @Test
    void shouldSuccessfullyConvertToEntityAttribute(){
        //Arrange
        PriceDataModel priceDataModel = new PriceDataModel(10.5, "EUR");
        String dbPrice = "10.5_EUR";

        //SUT
        PriceConverter converter = new PriceConverter();

        //Act
        PriceDataModel converted = converter.convertToEntityAttribute(dbPrice);

        //Assert
        assertEquals(priceDataModel,converted);

    }

    @Test
    void shouldReturnNullWhenConvertingNullPriceToDatabaseColumn() {
        //Arrange
        //SUT
        PriceConverter converter = new PriceConverter();

        //Act
        String result = converter.convertToDatabaseColumn(null);

        //Assert
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenConvertingNullDatabasePriceToEntityAttribute() {
        //Arrange
        //SUT
        PriceConverter converter = new PriceConverter();

        //Act
        PriceDataModel result = converter.convertToEntityAttribute(null);

        //Assert
        assertNull(result);
    }

    @Test
    void shouldThrowExceptionWhenDatabasePriceHasInvalidFormat() {
        //Arrange
        String dbPrice = "10.5_EUR_EXTRA";

        //SUT
        PriceConverter converter = new PriceConverter();

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> converter.convertToEntityAttribute(dbPrice)
        );

        //Assert
        assertEquals("Invalid price format: 10.5_EUR_EXTRA", exception.getMessage());
    }

}
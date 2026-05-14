package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply=true)
public class PriceConverter implements AttributeConverter<PriceDataModel,String> {

    private static final String separator = "_";

    @Override
    public String convertToDatabaseColumn(PriceDataModel priceDataModel){
        if (priceDataModel == null) return null;

        return priceDataModel.getNumericValue()
                + separator
                + priceDataModel.getCurrency();
    }

    @Override
    public PriceDataModel convertToEntityAttribute(String dbPrice){
        if (dbPrice == null) return null;

        String[] pieces = dbPrice.split(separator);

        if (pieces.length != 2) {
            throw new IllegalArgumentException("Invalid price format: " + dbPrice);
        }

        double numericValue = Double.parseDouble(pieces[0]);
        String currency = pieces[1];

        return new PriceDataModel(numericValue, currency);
    }


}
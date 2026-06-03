package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserIdConverter implements AttributeConverter<UserId, String> {

    @Override
    public String convertToDatabaseColumn(UserId attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public UserId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new UserId(new Email(dbData));
    }
}

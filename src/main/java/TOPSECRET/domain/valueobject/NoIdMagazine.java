package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.ValueObject;

public class NoIdMagazine implements MagazineId, ValueObject {

    @Override
    public String getIdentifier() {
        return "";
    }

}

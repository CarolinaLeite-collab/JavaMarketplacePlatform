package MITELOVERS.domain.valueobject;

import MITELOVERS.ddd.ValueObject;

public class NoIdentifier implements Identifier, ValueObject {

    private final String _noIdentifier;

    public NoIdentifier() {
        _noIdentifier ="no identifier";
    }

    @Override
    public String getIdentifier(){
        return _noIdentifier;
    }

    @Override
    public String toString() {
        return _noIdentifier;
    }

}

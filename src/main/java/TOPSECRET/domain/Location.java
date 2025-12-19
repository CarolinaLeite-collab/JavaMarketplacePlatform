package TOPSECRET.domain;

public class Location
{
    private final String _strStreet;
    private final String _strPostalCode;

    public Location( String strStreet, String strPostalCode ) throws InstantiationException
    {
        if( !isValidConstructorArguments(strStreet, strPostalCode))
            throw( new InstantiationException("Invalid Street or Postal Code"));

        this._strStreet = strStreet;
        this._strPostalCode = strPostalCode;
    }

    private boolean isValidConstructorArguments(String street, String postalCode )
    {
        if( street==null || street.isEmpty() )
            return false;
        return postalCode != null && !postalCode.isEmpty();
    }

    public String getStreet() {
        return _strStreet;
    }

    public String getPostalCode() {
        return _strPostalCode;
    }
}

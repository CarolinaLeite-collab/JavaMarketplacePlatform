package TOPSECRET.controller;

import PAI.domain.School;
import PAI.domain.Location;

public class DefineSchoolLocationController {

    private final School _school;

    public DefineSchoolLocationController(School school) {
        _school = school;
    }

    public Location defineHouseLocation( String strStreet, String strPostalCode ) throws InstantiationException {
        Location location = _school.defineLocation(strStreet, strPostalCode);

        return location;
    }
}

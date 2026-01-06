package TOPSECRET.domain;

import java.util.Objects;

/**
 *A registered entity (may be a person, company, or even an AI agent)
 *  on the platform who may play one or more roles.
 */

public class User {

    private final Name _name;
    private final Address _address;
    private final Email _email;
    private final Phone _phone;

    public User (Name _name, Address _address, Email _email, Phone _phone) {

        this._name = Objects.requireNonNull(_name, "name is required");
        this._address = Objects.requireNonNull(_address, "address is required");
        this._email = Objects.requireNonNull(_email, "email is required");
        this._phone = Objects.requireNonNull(_phone, "phoneNumber is required");

    }

        public Name getName(){
            return _name;
        }

        public Address getAddress(){
            return _address;
        }

        public Email getEmail(){
            return _email;
        }

        public Phone getPhone() {
            return _phone;
        }
}

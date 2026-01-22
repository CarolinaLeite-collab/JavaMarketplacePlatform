package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents a registered {@code User} in the system.
 * <p>
 * A user is identified uniquely by their {@link Email}.
 * </p>
 *
 * <p><b>Identity rule:</b> Two {@code User} instances are considered equal if they have the same
 * {@link Email} (see {@link #equals(Object)} and {@link #hashCode()}).</p>
 */

public class User {

    private final Name _name;
    private final Address _address;
    private final Email _email;
    private final Phone _phone;

    public User(Name name, Address address, Email email, Phone phone) {

        _name = Objects.requireNonNull(name, "name is required");
        _address = Objects.requireNonNull(address, "address is required");
        _email = Objects.requireNonNull(email, "email is required");
        _phone = Objects.requireNonNull(phone, "phoneNumber is required");

    }

    public User(Name name, Email email) {

        _name = Objects.requireNonNull(name, "name is required");
        _email = Objects.requireNonNull(email, "email is required");
        _address = null;
        _phone = null;
    }

    public Name getName() {
        return _name;
    }

    public Address getAddress() {
        return _address;
    }

    public String getEmail() {
        return _email.toString();
    }

    public Phone getPhone() {
        return _phone;
    }

    @Override
    public String toString() {
        return _name.toString();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return _email.equals(user._email);

    }

    @Override
    public int hashCode() {

        return _email.hashCode();

    }
}

package TOPSECRET.domain;

import TOPSECRET.ddd.DomainEntity;
import TOPSECRET.domain.valueobject.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a registered {@code User} in the system.
 * <p>
 * A user is identified uniquely by their {@link Email}.
 * </p>
 *
 * <p><b>Identity rule:</b> Two {@code User} instances are considered equal if they have the same
 * {@link Email} (see {@link #equals(Object)} and {@link #hashCode()}).</p>
 */

public class User implements DomainEntity<UserID> {

    private final Name _name;
    private final Address _address;
    private final Email _email;
    private final UserID _userId;
    private final Phone _phone;
    private final Set<Role> _roles = new HashSet<>();

    //package-private - only UserFactory creates
    User(Name name, Address address, Email email, Phone phone) {


        _name = Objects.requireNonNull(name, "Name is required");
        _address = address;
        _email = Objects.requireNonNull(email, "Email is required");
        _userId = new UserID(_email);
        _phone = phone;
        _roles.add(Role.USER); // default role
    }

    User(Name name, Email email) {
        this(name, null, email, null);
    }

    public void addRole(Role role) {
        _roles.add(Objects.requireNonNull(role));
    }

    public boolean hasRole(Role role) {
        return _roles.contains(role);
    }

    public boolean hasEmail(Email email) {
        return _email.equals(email);
    }

    public Set<Role> getRoles() {
        return Set.copyOf(_roles); // immutable view
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
    public UserID identity() {
        return _userId;
    }

    @Override
    public boolean sameAs(Object object) {
        if (!(object instanceof User other)) return false;
        return _userId.equals(other._userId);
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

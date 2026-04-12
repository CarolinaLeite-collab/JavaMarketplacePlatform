package TOPSECRET.domain.user;

import TOPSECRET.ddd.AggregateRoot;
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

public class User implements AggregateRoot<UserId> {

    private final Name _name;
    private final Address _address;
    private final Email _email;
    private final UserId _userId;
    private final Phone _phone;
    private final Set<Role> _roles = new HashSet<>();

    User(Name name, Address address, Email email, Phone phone) {

        _name = Objects.requireNonNull(name, "Name is required");
        _address = address;
        _email = Objects.requireNonNull(email, "Email is required");
        _userId = new UserId(_email);
        _phone = phone;
        _roles.add(Role.USER);
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
    public UserId identity() {
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
        return _userId.equals(user._userId);
    }

    @Override
    public int hashCode() {
        return _userId.hashCode();
    }
}

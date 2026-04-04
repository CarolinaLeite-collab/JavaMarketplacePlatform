package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Objects;

public final class UserId implements DomainId {

    private final Email _email;

    public UserId(Email email) {
        _email = Objects.requireNonNull(email, "Email is required");
    }

    public Email getEmail() { return _email; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserId other)) return false;
        return _email.equals(other._email);
    }

    @Override
    public int hashCode() {
        return _email.hashCode();
    }

    @Override
    public String toString() {
        return _email.toString();
    }
}

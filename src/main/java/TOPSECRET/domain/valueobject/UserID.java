package TOPSECRET.domain.valueobject;

import TOPSECRET.ddd.DomainId;

import java.util.Objects;

public class UserID implements DomainId {

    private final Email _email;

    public UserID(Email email) {
        _email = Objects.requireNonNull(email);
    }

    public Email getEmail() { return _email; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserID other)) return false;
        return _email.equals(other._email);
    }

    @Override
    public int hashCode() {
        return _email.hashCode();
    }

}

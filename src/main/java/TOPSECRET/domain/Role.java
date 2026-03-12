package TOPSECRET.domain;
/**
 * Defines the roles that can be assigned to a user within the system.
 * <p>
 * {@link Role#ADMIN} represents a system administrator with elevated privileges.
 * {@link Role#USER} is the default role assigned to every user during registration.
 * </p>
 * <p>
 * A user may hold multiple roles simultaneously.
 * </p>
 */

public enum Role {
        ADMIN,
        USER
}

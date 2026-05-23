package MITELOVERS.controller.root;

import MITELOVERS.domain.user.User;
import org.springframework.hateoas.Link;

import java.util.List;

/**
 * Contract for components that contribute HATEOAS links to the API entry point.
 *
 * <p>
 * Each resource area (genres, libraries, users, etc.) provides its own implementation.
 * The {@link RootController} collects all implementations injected by Spring and
 * builds the root representation from their combined output.
 * </p>
 *
 * <p>
 * Implementations must NOT perform persistence or business logic — only decide
 * which links to expose for the given user.
 * </p>
 */

public interface RootLinkProvider {

    List<Link> getLinks(User user);
}
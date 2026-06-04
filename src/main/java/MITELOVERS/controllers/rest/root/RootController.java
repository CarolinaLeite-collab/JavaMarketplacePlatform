package MITELOVERS.controllers.rest.root;

import MITELOVERS.applicationservices.UserService;
import MITELOVERS.domain.user.User;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST controller responsible for exposing the API root resource.
 * <p>
 * Retrieves the available API links for a given user
 * and exposes them as a HAL-based hypermedia response.
 * </p>
 * <p>
 * Links are provided dynamically through {@link RootLinkProvider}
 * implementations and injected by Spring through dependency injection.
 * </p>
 * <p>
 * <b>Temporary:</b> the user is currently identified through the
 * {@code ?email=} query parameter.
 * This mechanism will be replaced by JWT authentication once
 * authentication support is introduced.
 * </p>
 */

@RestController
@RequestMapping("/api")
public class RootController {

    private final List<RootLinkProvider> _linkProviders;
    private final UserService _userService;

    public RootController(List<RootLinkProvider> linkProviders, UserService userService) {

        _linkProviders = linkProviders;
        _userService = userService;

    }

    @RequestMapping(method = RequestMethod.OPTIONS, produces = MediaTypes.HAL_JSON_VALUE)
    public RepresentationModel<?> root(@RequestParam String email){

        User user = _userService.getUserByEmail(email);

        RepresentationModel<?> root = new RepresentationModel<>();

        root.add(
                linkTo(methodOn(RootController.class).root(email))
                        .withSelfRel()
        );

        _linkProviders.stream()
                .flatMap(provider -> provider.getLinks(user).stream())
                .forEach(root::add);

        return root;
    }
}

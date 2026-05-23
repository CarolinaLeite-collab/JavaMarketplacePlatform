package MITELOVERS.controller.root;

import MITELOVERS.domain.repository.IUserRepo;
import MITELOVERS.domain.user.User;
import MITELOVERS.domain.valueobject.Email;
import MITELOVERS.domain.valueobject.UserId;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;

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

    private final List<RootLinkProvider> linkProviders;
    private final IUserRepo userRepo;

    public RootController(List<RootLinkProvider> linkProviders,
                          IUserRepo userRepo) {
        this.linkProviders = linkProviders;
        this.userRepo = userRepo;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public RepresentationModel<?> root(@RequestParam String email){

        User user = userRepo.ofIdentity(new UserId(new Email(email)))
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        RepresentationModel<?> root = new RepresentationModel<>();

        root.add(
                linkTo(methodOn(RootController.class).root(email))
                        .withSelfRel()
        );

        linkProviders.stream()
                .flatMap(provider -> provider.getLinks(user).stream())
                .forEach(root::add);

        return root;
    }
}

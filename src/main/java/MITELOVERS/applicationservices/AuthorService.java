package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorService {

    private final IAuthorRepo _iAuthorRepo;
    private final AuthorFactory _authorFactory;

    public AuthorService(IAuthorRepo iAuthorRepo, AuthorFactory authorFactory) {
        _iAuthorRepo = Objects.requireNonNull(iAuthorRepo, "AuthorRepo is required");
        _authorFactory = Objects.requireNonNull(authorFactory, "AuthorFactory is required");
    }

    public Author registerAuthor(String authorName) {
        Name name = new Name(authorName);
        Author newAuthor = _authorFactory.createAuthor(name);

        if (_iAuthorRepo.containsOfIdentity(newAuthor.identity())) {
            throw new IllegalStateException("Author already exists in the repository");
        }

        return _iAuthorRepo.save(newAuthor);
    }

    public Iterable<Author> getAllAuthors() {
        return _iAuthorRepo.findAll();
    }

    public Optional<Author> getAuthorById(String id) {
        return _iAuthorRepo.ofIdentity(new AuthorId(id));
    }

    public Iterable<AuthorId> getAuthorsId() {
        List<AuthorId> ids = new ArrayList<>();
        for (Author author : _iAuthorRepo.findAll()) {
            ids.add(author.identity());
        }
        return ids;
    }
}
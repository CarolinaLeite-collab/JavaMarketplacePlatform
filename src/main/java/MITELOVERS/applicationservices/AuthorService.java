package MITELOVERS.applicationservices;

import MITELOVERS.domain.author.Author;
import MITELOVERS.domain.author.AuthorFactory;
import MITELOVERS.domain.repository.IAuthorRepo;
import MITELOVERS.domain.valueobject.AuthorId;
import MITELOVERS.domain.valueobject.Name;
import MITELOVERS.dto.AuthorResponseDTO;
import MITELOVERS.mapper.AuthorResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthorService {

    private final IAuthorRepo _iAuthorRepo;
    private final AuthorFactory _authorFactory;
    private final AuthorResponseDTOMapper _authorResponseDTOMapper;

    public AuthorService(IAuthorRepo iAuthorRepo,
                         AuthorFactory authorFactory,
                         AuthorResponseDTOMapper mapper) {

        _iAuthorRepo = Objects.requireNonNull(iAuthorRepo, "AuthorRepo is required");
        _authorFactory = Objects.requireNonNull(authorFactory, "AuthorFactory is required");
        _authorResponseDTOMapper = Objects.requireNonNull(mapper, "AuthorDTOAssembler is required");
    }

    public AuthorResponseDTO registerAuthor(String authorName) {
        Name name = new Name(authorName);
        Author newAuthor = _authorFactory.createAuthor(name);

        if (_iAuthorRepo.containsOfIdentity(newAuthor.identity())) {
            throw new IllegalStateException("Author already exists in the repository");
        }

        Author savedAuthor = _iAuthorRepo.save(newAuthor);

        return _authorResponseDTOMapper.toModel(savedAuthor);
    }

    public List<AuthorResponseDTO> getAllAuthors() {
        Iterable<Author> authors = _iAuthorRepo.findAll();

        List<AuthorResponseDTO> response = new ArrayList<>();

        for (Author author : authors) {
            response.add(_authorResponseDTOMapper.toModel(author));
        }

        return response;
    }

    public Iterable<AuthorId> getAuthorsId() {
        return _iAuthorRepo.findAllKeys();
    }

}

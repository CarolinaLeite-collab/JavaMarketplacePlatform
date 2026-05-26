package MITELOVERS.applicationservices;

import MITELOVERS.domain.genre.Genre;
import MITELOVERS.domain.genre.GenreFactory;
import MITELOVERS.domain.repository.IGenreRepo;
import MITELOVERS.dto.GenreResponseDTO;
import MITELOVERS.mapper.GenreResponseDTOMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GenreService {

    private final IGenreRepo _iGenreRepo;
    private final GenreFactory _genreFactory;
    private final GenreResponseDTOMapper _genreResponseDTOMapper;

    public GenreService(IGenreRepo iGenreRepo,
                        GenreFactory genreFactory,
                        GenreResponseDTOMapper mapper) {

        _iGenreRepo = Objects.requireNonNull(iGenreRepo, "GenreRepo is required");
        _genreFactory = Objects.requireNonNull(genreFactory, "GenreFactory is required");
        _genreResponseDTOMapper = Objects.requireNonNull(mapper, "GenreDTOAssembler is required");
    }

    public GenreResponseDTO registerGenre(String genreName) {
        Genre newGenre = _genreFactory.createGenre(genreName);

        if (_iGenreRepo.containsOfIdentity(newGenre.identity())) {
            throw new IllegalStateException("Genre already exists in the repository");
        }

        Genre savedGenre = _iGenreRepo.save(newGenre);

        return _genreResponseDTOMapper.toResponseDTO(savedGenre);
    }

    public List<GenreResponseDTO> getAllGenres() {
        Iterable<Genre> genres = _iGenreRepo.findAll();

        List<GenreResponseDTO> response = new ArrayList<>();

        for (Genre genre : genres) {
            response.add(_genreResponseDTOMapper.toResponseDTO(genre));
        }

        return response;
    }
}


package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Publications")
public class PublicationDataModel {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "title", nullable = false, unique = true)
    private String Title;
    @Column(name = "authorId", nullable = false, unique = true)
    private String AuthorId;
    @Column(name = "releaseYear", nullable = false, unique = true)
    private String ReleaseYear;
    @Column(name = "genreId", nullable = false, unique = true)
    private String GenreId;
}

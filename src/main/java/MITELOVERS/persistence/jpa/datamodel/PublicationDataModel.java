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
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "title", nullable = false)
    private String Title;
    @Column(name = "authorId", nullable = false)
    private String AuthorId;
    @Column(name = "releaseYear", nullable = false)
    private String ReleaseYear;
    @Column(name = "genreId", nullable = false)
    private String GenreId;
}

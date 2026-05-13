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
    private String title;
    @Column(name = "author_id", nullable = false)
    private String authorId;
    @Column(name = "release_year", nullable = false)
    private String releaseYear;
    @Column(name = "genre_id", nullable = false)
    private String genreId;
}

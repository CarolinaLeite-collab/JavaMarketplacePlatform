package MITELOVERS.persistence.jpa.datamodel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing Library information,
 * allowing its persistence in a database.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Libraries")
public class LibraryDataModel {

    @Id
    private String libraryId;

    @ElementCollection
    @CollectionTable(
            name = "LibraryItemIds",
            joinColumns = @JoinColumn(name = "libraryId")
    )
    @Column(name = "itemId")
    private List<String> itemIds;
}

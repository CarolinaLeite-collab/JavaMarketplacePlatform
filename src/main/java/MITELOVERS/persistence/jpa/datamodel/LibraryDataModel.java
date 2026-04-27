package MITELOVERS.persistence.jpa.datamodel;

import java.util.List;

package MITELOVERS.datamodel;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
    private String _libraryId;

    @Column(nullable = false)
    private String _userId;

    @ElementCollection
    @CollectionTable(
            name = "LibraryItemIds",
            joinColumns = @JoinColumn(name = "library_id")
    )
    @Column(name = "item_id")
    private List<String> _itemIds;
}

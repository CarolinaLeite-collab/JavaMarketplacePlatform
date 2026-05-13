package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.listofitems.ListOfItems;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data model object representing {@link ListOfItems} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_of_items")
public class ListOfItemsDataModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String listOfItemsId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @ElementCollection
    @CollectionTable(
            name = "list_of_items_item_ids",
            joinColumns = @JoinColumn(name = "list_of_items_id")
    )
    @Column(name = "item_id")
    private List<String> itemIds;
}
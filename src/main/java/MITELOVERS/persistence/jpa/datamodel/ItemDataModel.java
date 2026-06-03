package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Data model object representing {@link Item} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class ItemDataModel {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "edition_id", nullable = false)
    private String editionId;

    @Column(name = "condition", nullable = false)
    private String condition;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "sale_status", nullable = false)
    private String saleStatus;

    @Column(name = "picture", nullable = true)
    private String picture;

}

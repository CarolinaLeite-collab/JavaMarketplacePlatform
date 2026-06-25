package MITELOVERS.persistence.jpa.datamodel;

import MITELOVERS.domain.sale.Sale;
import MITELOVERS.domain.valueobject.SaleSaleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data model object representing {@link Sale} information,
 * allowing its persistence in a database.
 */

@Generated
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Sales")
public class SaleDataModel {

    @Id
    @Column(name = "sale_id")
    private String saleId;

    @Column(name = "buyer_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SaleSaleStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleLineDataModel> saleLines = new ArrayList<>();
}


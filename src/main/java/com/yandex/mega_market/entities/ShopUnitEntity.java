package com.yandex.mega_market.entities;

import com.yandex.mega_market.entities.enums.ShopUnitType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "shop_unit" )
public class ShopUnitEntity {

    @Id
    @Column( name = "id", updatable = false, nullable = false )
    private UUID id;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "date", nullable = false )
    private LocalDateTime date;

    @Enumerated( value = EnumType.STRING )
    @Column( name = "type", updatable = false )
    private ShopUnitType type;

    @Column( name = "price" )
    private Long price;


    @Column( name = "last_price_updated_time" )
    private LocalDateTime lastPriceUpdatedTime;

    @ManyToOne( cascade = CascadeType.REFRESH, fetch = FetchType.LAZY )
    @JoinColumn( name = "parent_id" )
    private ShopUnitEntity parent;

    @OneToMany( cascade = { CascadeType.MERGE, CascadeType.REFRESH }, mappedBy = "parent" )
    private List<ShopUnitEntity> children = new ArrayList<>();

    public void setParent( ShopUnitEntity parent ) {
        if ( Objects.nonNull( parent ) ) {
            if ( !parent.getChildren().contains( this ) ) {
                parent.getChildren().add( this );

            }
            if ( this.getId() == parent.getId() ) {
                parent = null;
            }
        } else {
            if ( Objects.nonNull( this.parent ) ) {
                this.parent.getChildren().remove( this );
            }
        }
        this.parent = parent;
    }

}

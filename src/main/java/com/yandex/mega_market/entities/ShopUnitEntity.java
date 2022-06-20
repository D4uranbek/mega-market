package com.yandex.mega_market.entities;

import com.yandex.mega_market.DTOs.ShopUnitType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "shop_unit", indexes = {
        @Index( name = "shop_unit_id_key",
                columnList = "id", unique = true
        )
} )
public class ShopUnitEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "uid", nullable = false )
    private Long uid;

    @Column( name = "id", nullable = false )
    private UUID id;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "date", nullable = false )
    @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
    private Date date;

    @Column( name = "parent_id" )
    private UUID parentId;

    @Enumerated( EnumType.STRING )
    private ShopUnitType type;

    private Long price;

}

package com.yandex.mega_market.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
class ShopUnitStatisticUnit {

    @Schema(
            example = "3fa85f64-5717-4562-b3fc-2c963f66a333",
            required = true,
            description = "Уникальный идентфикатор"
    )
    @NotNull
    @Valid
    private UUID id;

    @Schema(
            required = true,
            description = "Имя элемента"
    )
    @NotNull
    private String name;

    @Schema(
            example = "3fa85f64-5717-4562-b3fc-2c963f66a333",
            description = "UUID родительской категории"
    )
    @Valid
    private UUID parentId;

    @Schema( required = true )
    @NotNull
    @Valid
    private ShopUnitType type;

    @Schema( description = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). " +
            "Если цена является не целым числом, округляется в меньшую сторону до целого числа. " +
            "Если категория не содержит товаров цена равна null." )
    private Long price;

    @Schema(
            required = true,
            description = "Время последнего обновления элемента."
    )
    @NotNull
    @Valid
    @DateTimeFormat( iso = ISO.DATE_TIME )
    private Date date;

}


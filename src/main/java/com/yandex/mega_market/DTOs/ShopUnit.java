package com.yandex.mega_market.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
public class ShopUnit {

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
            description = "Имя категории"
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

    @Schema( description = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null." )
    private Long price;

    @Schema( description = "Список всех дочерних товаров/категорий. Для товаров поле равно null." )
    @Valid
    private List<ShopUnit> children;

    @Schema(
            example = "2022-05-28T21:12:01Z",
            required = true,
            description = "Время последнего обновления элемента."
    )
    @NotNull
    @Valid
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" )
    LocalDateTime date;

}


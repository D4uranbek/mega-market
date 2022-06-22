package com.yandex.mega_market.DTOs;

import com.yandex.mega_market.entities.enums.ShopUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
public class ShopUnitImport {

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
            description = "Имя элемента."
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

    @Schema( description = "Целое число, для категорий поле должно содержать null." )
    private Long price;

}

package com.yandex.mega_market.controllers;

import com.yandex.mega_market.DTOs.ShopUnitStatisticResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 21.06.2022
 */
@RestController
@RequiredArgsConstructor
@Tag( name = "Дополнительные задачи" )
public class ShopUnitAdditionalTasksController {

    @Operation(
            description = "Получение списка **товаров**, цена которых была обновлена за последние 24 часа включительно [now() - 24h, now()] от времени переданном в запросе."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список товаров, цена которых была обновлена."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            )
    } )
    @GetMapping( value = "/sales", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ShopUnitStatisticResponse> getPriceUpdatedShopUnit( @Parameter( description = "Дата и время запроса. Дата должна обрабатываться согласно ISO 8601 (такой придерживается OpenAPI). Если дата не удовлетворяет данному формату, необходимо отвечать 400", required = true )
                                                                              @RequestParam( value = "date" )
                                                                              @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
                                                                              @NotNull
                                                                              @Valid
                                                                              Date date ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }


    @Operation(
            description = "Получение статистики (истории обновлений) по товару/категории за заданный полуинтервал [from, to)."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Статистика по элементу."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный формат запроса или некорректные даты интервала."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория/товар не найден."
            )
    } )
    @GetMapping( value = "/node/{id}/statistic", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ShopUnitStatisticResponse> getStatisticsById( @Parameter( description = "UUID товара/категории для которой будет отображаться статистика", required = true )
                                                                        @PathVariable( "id" )
                                                                        UUID id,

                                                                        @Parameter( description = "Дата и время начала интервала, для которого считается статистика." )
                                                                        @RequestParam( value = "dateStart", required = false )
                                                                        @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
                                                                        @Valid Date dateStart,

                                                                        @Parameter( description = "Дата и время конца интервала, для которого считается статистика." )
                                                                        @RequestParam( value = "dateEnd", required = false )
                                                                        @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
                                                                        @Valid Date dateEnd ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }

}

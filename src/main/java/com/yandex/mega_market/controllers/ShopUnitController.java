package com.yandex.mega_market.controllers;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.DTOs.ShopUnitStatisticResponse;
import com.yandex.mega_market.services.ShopUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@RestController
@RequiredArgsConstructor
@ResponseStatus( HttpStatus.OK )
public class ShopUnitController {

    private final ShopUnitService service;

    /**
     * POST /imports
     * Импортирует новые товары и/или категории.
     *
     * @param shopUnitImportRequest (optional)
     * @return Вставка или обновление прошли успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     */
    @Operation(
            description = "Импортирует новые товары и/или категории.",
            tags = { "Базовые задачи" }
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Вставка или обновление прошли успешно."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            )
    } )
    @PostMapping( value = "/imports",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseStatus( HttpStatus.OK )
    public ResponseEntity<Void> importNewShopUnit( @RequestBody( required = false )
                                                   @Valid
                                                   ShopUnitImportRequest shopUnitImportRequest ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }


    /**
     * DELETE /delete/{id}
     * Удалить элемент по идентификатору. При удалении категории удаляются все дочерние элементы.
     *
     * @param id Идентификатор (required)
     * @return Удаление прошло успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     * or Категория/товар не найден. (status code 404)
     */
    @Operation(
            description = "Удалить элемент по идентификатору.",
            tags = { "Базовые задачи", }
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаление прошло успешно."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория/товар не найден."
            )
    } )
    @DeleteMapping( value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Void> deleteElement( @Parameter( description = "Идентификатор", required = true )
                                               @PathVariable( "id" )
                                               UUID id ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }


    /**
     * GET /nodes/{id}
     * Получить информацию об элементе по идентификатору.
     *
     * @param id Идентификатор (required)
     * @return Информация об элементе. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     * or Категория/товар не найден. (status code 404)
     */
    @Operation(
            description = "Получить информацию об элементе по идентификатору.",
            tags = { "Базовые задачи" }
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация об элементе."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория/товар не найден."
            )
    } )
    @GetMapping( value = "/nodes/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ShopUnit> getElementInfo( @Parameter( description = "Идентификатор элемента", required = true )
                                                    @PathVariable( "id" )
                                                    UUID id ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }

    @Operation(
            description = "Получение списка **товаров**, цена которых была обновлена за последние 24 часа включительно [now() - 24h, now()] от времени переданном в запросе.",
            tags = { "Дополнительные задачи" }
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
                                                                              @RequestParam( value = "date", required = true )
                                                                              @DateTimeFormat( iso = DateTimeFormat.ISO.DATE_TIME )
                                                                              @NotNull
                                                                              @Valid
                                                                              Date date ) {
        return new ResponseEntity<>( HttpStatus.NOT_IMPLEMENTED );
    }


    @Operation(
            description = "Получение статистики (истории обновлений) по товару/категории за заданный полуинтервал [from, to).",
            tags = { "Дополнительные задачи" }
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
                                                                        @PathVariable( "id" ) UUID id,

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

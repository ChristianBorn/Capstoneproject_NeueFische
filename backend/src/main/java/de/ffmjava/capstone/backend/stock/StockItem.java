package de.ffmjava.capstone.backend.stock;

import lombok.With;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@With
public record StockItem(
        String id,
        @NotBlank(message = "Feld \"Name/Bezeichnung\" darf nicht leer sein")
        String name,
        @NotNull (message = "Feld \"Typ\" darf nicht leer sein")
        StockType type,
        @Min(0)
        @NotNull (message = "Feld \"Menge\" in Kilogramm darf nicht leer sein")
        BigDecimal amountInStock,
        @Min(0)
        @NotNull (message = "Feld \"Preis\" pro Kilogramm darf nicht leer sein")
        BigDecimal pricePerKilo
) {
}

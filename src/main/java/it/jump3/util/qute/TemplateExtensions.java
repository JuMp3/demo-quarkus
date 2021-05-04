package it.jump3.util.qute;

import io.quarkus.qute.TemplateExtension;
import it.jump3.controller.model.ItemDto;

import java.math.BigDecimal;

@TemplateExtension
public class TemplateExtensions {

    public static BigDecimal discountedPrice(ItemDto item) {
        return item.getPrice().multiply(new BigDecimal("0.9"));
    }
}

package it.jump3.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto implements Serializable {

    private static final long serialVersionUID = 3990667799213170821L;

    @QueryParam("name")
    private String name;

    @QueryParam("price")
    private BigDecimal price;
}

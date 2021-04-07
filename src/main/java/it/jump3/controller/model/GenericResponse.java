package it.jump3.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GenericResponse implements Serializable {
    private static final long serialVersionUID = 2353211012378915386L;
    private Object value;
}

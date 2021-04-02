package it.jump3.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentFile implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 16742650496181381L;
    
    private String fileName;
    private String mime;
    private byte[] source;
}

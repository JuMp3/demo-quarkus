package it.jump3.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilesResponse implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4946164821458568954L;
    
    private List<AttachmentFile> files;
}

package it.jump3.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadResponse implements Serializable {

    private static final long serialVersionUID = -5838532805857749968L;

    private File file;
    private String fileName;
}

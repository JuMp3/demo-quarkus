package it.jump3.file;

import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;

@NoArgsConstructor
public class FileUploadForm implements Serializable {

    private static final long serialVersionUID = 1450957024741763432L;

    @Schema(description = "File to upload", required = true)
    @NotNull(message = "File may not be null")
    private byte[] data;

    @Schema(description = "Filename to upload", required = true)
    @NotBlank(message = "Filename may not be null")
    private String fileName;

    public byte[] getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public void setData(byte[] data) {
        this.data = data;
    }

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

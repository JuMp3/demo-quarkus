
package it.jump3.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.jump3.util.DateUtil;
import it.jump3.util.JsonLocalDateTimeDeserializer;
import it.jump3.util.JsonLocalDateTimeSerializer;
import it.jump3.util.Utility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 2101299941857781568L;

    @Schema(required = true, description = "Correlation ID del flusso andato in errore")
    private String correlationId;

    @JsonProperty("timestamp")
    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    @Schema(required = true, description = "Orario dell'errore", type = SchemaType.STRING, format = "date-time")
    private LocalDateTime localDateTime = DateUtil.nowLocalDateTimeItaly();

    @JsonProperty("httpStatusCode")
    @Schema(required = true, description = "HTTP status code dell'errore")
    private int httpStatusCode;

    @JsonProperty("errorCode")
    @Schema(required = true, description = "Codice errore.")
    private String errorCode;

    @JsonIgnore
    private List<String> messages = new LinkedList<>();

    @Override
    public String toString() {
        return "{\n" +
                "    correlationId: " + this.correlationId + "\n" +
                "    localDateTime: " + this.toIndentedString(this.localDateTime != null ? DateUtil.getFromLocalDateTimeDefault(this.localDateTime) : null) + "\n" +
                "    httpStatusCode: " + this.toIndentedString(this.httpStatusCode) + "\n" +
                "    errorDescription: " + this.toIndentedString(Utility.convertListInString(this.messages, "{", "}")) + "\n" +
                "    errorCode: " + toIndentedString(this.errorCode) + "\n" +
                "}";
    }

    @JsonProperty("errorDescription")
    @Schema(required = true, description = "Messaggi dell'errore")
    public String getErrorDescription() {
        return CollectionUtils.isEmpty(messages) ? null : Utility.convertListInString(messages, ", ");
    }

    @JsonIgnore
    protected String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

    @JsonIgnore
    public String getMessage() {
        return !CollectionUtils.isEmpty(getMessages()) ? Utility.convertListInString(getMessages(), ", ") : null;
    }

    @JsonIgnore
    public void setMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            this.messages = new LinkedList<>();
            this.messages.add(message);
        }
    }

    @JsonIgnore
    public void addMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            if (this.messages == null) {
                this.messages = new LinkedList<>();
            }
            this.messages.add(message);
        }
    }
}

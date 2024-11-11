package mavmi.parameters_management_system.common.dto.server.inner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mavmi.parameters_management_system.common.parameter.api.PARAMETER_TYPE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Value {
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private String value;
    @JsonProperty("type")
    private PARAMETER_TYPE type;
}

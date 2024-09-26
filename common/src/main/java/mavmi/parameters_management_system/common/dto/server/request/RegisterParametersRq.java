package mavmi.parameters_management_system.common.dto.server.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mavmi.parameters_management_system.common.dto.server.inner.Value;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterParametersRq {
    @JsonProperty("values")
    private List<Value> values;
}

package mavmi.parameters_management_system.common.dto.server.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mavmi.parameters_management_system.common.dto.server.request.inner.Value;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetParameterRs {
    @JsonProperty("value")
    private Value value;
}

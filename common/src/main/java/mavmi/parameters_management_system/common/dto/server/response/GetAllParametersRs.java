package mavmi.parameters_management_system.common.dto.server.response;

import lombok.*;
import mavmi.parameters_management_system.common.dto.server.request.inner.Value;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllParametersRs {
    private List<Value> parameters;
}

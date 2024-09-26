package mavmi.parameters_management_system.client.plugin.impl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mavmi.parameters_management_system.common.dto.server.inner.Value;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesFile {
    @JsonProperty("values")
    private List<Value> values;
}

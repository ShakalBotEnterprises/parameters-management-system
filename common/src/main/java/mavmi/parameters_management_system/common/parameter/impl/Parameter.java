package mavmi.parameters_management_system.common.parameter.impl;

import lombok.*;
import mavmi.parameters_management_system.common.parameter.api.PARAMETER_TYPE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    private String name;
    private String value;
    private PARAMETER_TYPE type;

    public long getLong() {
        return Long.parseLong(value);
    }

    public String getString() {
        return value;
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(value);
    }
}

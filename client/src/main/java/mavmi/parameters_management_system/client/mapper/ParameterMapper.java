package mavmi.parameters_management_system.client.mapper;

import mavmi.parameters_management_system.common.dto.server.inner.Value;
import mavmi.parameters_management_system.common.parameter.impl.Parameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParameterMapper {

    Value parameterToValueDto(Parameter parameter);
    Parameter valueDtoToParameter(Value value);
}

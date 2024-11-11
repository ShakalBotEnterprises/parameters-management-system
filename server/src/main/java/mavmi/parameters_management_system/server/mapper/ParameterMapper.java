package mavmi.parameters_management_system.server.mapper;

import mavmi.parameters_management_system.common.dto.server.inner.Value;
import mavmi.parameters_management_system.server.database.model.PmsModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParameterMapper {

    Value pmsModelToValueJson(PmsModel model);
    PmsModel valueJsonToPmsModel(Value value);
}

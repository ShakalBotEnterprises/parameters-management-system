package mavmi.parameters_management_system.client.plugin.api;

import mavmi.parameters_management_system.common.parameter.impl.Parameter;

import java.util.List;

public interface ParameterPlugin {

    Parameter getParameter(String name);
    List<Parameter> getAllParameters();
    boolean updateParameter(Parameter parameter);
}

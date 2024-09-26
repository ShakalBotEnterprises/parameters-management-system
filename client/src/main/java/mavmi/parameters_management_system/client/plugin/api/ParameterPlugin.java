package mavmi.parameters_management_system.client.plugin.api;

import mavmi.parameters_management_system.common.parameter.impl.Parameter;

public interface ParameterPlugin {

    Parameter getParameter(String name);
}

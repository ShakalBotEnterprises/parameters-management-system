package mavmi.parameters_management_system.client.plugin.impl.remote;

import lombok.RequiredArgsConstructor;
import mavmi.parameters_management_system.client.httpClient.HttpClient;
import mavmi.parameters_management_system.client.plugin.api.ParameterPlugin;
import mavmi.parameters_management_system.client.plugin.impl.local.ResourcesParameterPlugin;
import mavmi.parameters_management_system.common.parameter.impl.Parameter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoteParameterPlugin implements ParameterPlugin {

    private final HttpClient httpClient;
    private final ResourcesParameterPlugin resourcesParameterPlugin;

    @Override
    public Parameter getParameter(String name) {
        Parameter parameter = httpClient.getParameter(name);
        return (parameter == null) ? resourcesParameterPlugin.getParameter(name) : parameter;
    }

    @Override
    public List<Parameter> getAllParameters() {
        List<Parameter> parameters = httpClient.getAllParameters();
        return (parameters.isEmpty()) ? resourcesParameterPlugin.getAllParameters() : parameters;
    }

    @Override
    public boolean updateParameter(Parameter parameter) {
        boolean returnValue = httpClient.updateParameter(parameter);
        resourcesParameterPlugin.updateParameter(parameter);

        return returnValue;
    }
}

package mavmi.parameters_management_system.client.plugin.impl.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mavmi.parameters_management_system.client.httpClient.HttpClient;
import mavmi.parameters_management_system.client.mapper.ParameterMapper;
import mavmi.parameters_management_system.client.plugin.api.ParameterPlugin;
import mavmi.parameters_management_system.client.plugin.impl.dto.ResourcesFile;
import mavmi.parameters_management_system.common.dto.server.request.inner.Value;
import mavmi.parameters_management_system.common.parameter.impl.Parameter;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourcesParameterPlugin implements ParameterPlugin {

    private final Map<String, Parameter> parameters = new HashMap<>();
    private final HttpClient httpClient;
    private final ParameterMapper mapper;

    @org.springframework.beans.factory.annotation.Value("classpath:pms/parameters.json")
    private Resource resource;

    @PostConstruct
    public void registerParameters() {
        String rawPropertiesJson = readResourcesFile();
        ResourcesFile resourcesFile;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            resourcesFile = objectMapper.readValue(rawPropertiesJson, ResourcesFile.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Parameter> parametersToRegister = new ArrayList<>();
        for (Value value : resourcesFile.getValues()) {
            Parameter parameter = mapper.valueDtoToParameter(value);
            parametersToRegister.add(parameter);
            parameters.put(parameter.getName(), parameter);
        }

        httpClient.registerParameters(parametersToRegister);
    }

    @Override
    @Nullable
    public Parameter getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public boolean updateParameter(Parameter parameter) {
        if (!parameters.containsKey(parameter.getName())) {
            return false;
        } else {
            parameters.put(parameter.getName(), parameter);
            return true;
        }
    }

    private String readResourcesFile() {

        try (BufferedReader reader = new BufferedReader( new InputStreamReader( resource.getInputStream() ))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

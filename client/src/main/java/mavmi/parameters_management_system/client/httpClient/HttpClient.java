package mavmi.parameters_management_system.client.httpClient;

import lombok.extern.slf4j.Slf4j;
import mavmi.parameters_management_system.client.mapper.ParameterMapper;
import mavmi.parameters_management_system.common.dto.server.request.GetParameterRq;
import mavmi.parameters_management_system.common.dto.server.request.RegisterParametersRq;
import mavmi.parameters_management_system.common.dto.server.request.UpdateParameterRq;
import mavmi.parameters_management_system.common.dto.server.response.GetAllParametersRs;
import mavmi.parameters_management_system.common.dto.server.response.GetParameterRs;
import mavmi.parameters_management_system.common.parameter.impl.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class HttpClient {

    private final RestTemplate restTemplate;
    private final ParameterMapper mapper;
    private final String baseUrl;
    private final String getParameterEndpoint;
    private final String getAllParametersEndpoint;
    private final String registerParametersEndpoint;
    private final String updateParameterEndpoint;

    public HttpClient(
            SslBundles sslBundles,
            RestTemplateBuilder restTemplateBuilder,
            ParameterMapper mapper,
            @Value("${pms.client.http-client.ssl-bundle-name}") String sslBundleName,
            @Value("${pms.client.http-client.url.base}") String baseUrl,
            @Value("${pms.client.http-client.endpoint.get-parameter}") String getParameterEndpoint,
            @Value("${pms.client.http-client.endpoint.get-all-parameters}") String getAllParametersEndpoint,
            @Value("${pms.client.http-client.endpoint.register-parameters}") String registerParametersEndpoint,
            @Value("${pms.client.http-client.endpoint.update-parameter}") String updateParameterEndpoint
    ) {
        try {
            restTemplateBuilder = restTemplateBuilder.setSslBundle(sslBundles.getBundle(sslBundleName));
        } catch (NoSuchSslBundleException e) {
            log.error(e.getMessage(), e);
        }

        this.restTemplate = restTemplateBuilder.build();
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.getParameterEndpoint = getParameterEndpoint;
        this.getAllParametersEndpoint = getAllParametersEndpoint;
        this.registerParametersEndpoint = registerParametersEndpoint;
        this.updateParameterEndpoint = updateParameterEndpoint;
    }

    @Nullable
    public Parameter getParameter(String name) {
        mavmi.parameters_management_system.common.dto.server.request.inner.Value value = mavmi.parameters_management_system.common.dto.server.request.inner.Value
                .builder()
                .name(name)
                .build();
        GetParameterRq requestBody = GetParameterRq.builder()
                .value(value)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GetParameterRq> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            ResponseEntity<GetParameterRs> responseEntity = restTemplate.postForEntity(baseUrl + getParameterEndpoint, httpEntity, GetParameterRs.class);
            if (responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()))) {
                return null;
            } else {
                return mapper.valueDtoToParameter(responseEntity.getBody().getValue());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Parameter> getAllParameters() {
        try {
            ResponseEntity<GetAllParametersRs> responseEntity = restTemplate.getForEntity(baseUrl + getAllParametersEndpoint, GetAllParametersRs.class);
            if (!responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(HttpStatus.OK.value()))) {
                return Collections.emptyList();
            } else {
                return responseEntity.getBody()
                        .getParameters()
                        .stream()
                        .map(mapper::valueDtoToParameter)
                        .toList();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public void registerParameters(List<Parameter> parameters) {
        List<mavmi.parameters_management_system.common.dto.server.request.inner.Value> values = parameters
                .stream()
                .map(mapper::parameterToValueDto)
                .toList();
        RegisterParametersRq requestBody = RegisterParametersRq
                .builder()
                .values(values)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterParametersRq> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            restTemplate.postForEntity(baseUrl + registerParametersEndpoint, httpEntity, String.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean updateParameter(Parameter parameter) {
        mavmi.parameters_management_system.common.dto.server.request.inner.Value value = mapper.parameterToValueDto(parameter);
        UpdateParameterRq requestBody = UpdateParameterRq
                .builder()
                .value(value)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UpdateParameterRq> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {
            restTemplate.postForEntity(baseUrl + updateParameterEndpoint, httpEntity, String.class);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}

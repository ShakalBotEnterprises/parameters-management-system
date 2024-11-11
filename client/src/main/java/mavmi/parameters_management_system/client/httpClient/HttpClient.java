package mavmi.parameters_management_system.client.httpClient;

import lombok.extern.slf4j.Slf4j;
import mavmi.parameters_management_system.client.mapper.ParameterMapper;
import mavmi.parameters_management_system.common.dto.server.request.GetParameterRq;
import mavmi.parameters_management_system.common.dto.server.request.RegisterParametersRq;
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

import java.util.List;

@Slf4j
@Component
public class HttpClient {

    private final RestTemplate restTemplate;
    private final ParameterMapper mapper;
    private final String baseUrl;
    private final String getPropertyEndpoint;
    private final String registerPropertiesEndpoint;

    public HttpClient(
            SslBundles sslBundles,
            RestTemplateBuilder restTemplateBuilder,
            ParameterMapper mapper,
            @Value("${pms.client.http-client.ssl-bundle-name}") String sslBundleName,
            @Value("${pms.client.http-client.url.base}") String baseUrl,
            @Value("${pms.client.http-client.endpoint.get-property}") String getPropertyEndpoint,
            @Value("${pms.client.http-client.endpoint.register-properties}") String registerPropertiesEndpoint
    ) {
        try {
            restTemplateBuilder = restTemplateBuilder.setSslBundle(sslBundles.getBundle(sslBundleName));
        } catch (NoSuchSslBundleException e) {
            log.error(e.getMessage(), e);
        }

        this.restTemplate = restTemplateBuilder.build();
        this.mapper = mapper;
        this.baseUrl = baseUrl;
        this.getPropertyEndpoint = getPropertyEndpoint;
        this.registerPropertiesEndpoint = registerPropertiesEndpoint;
    }

    @Nullable
    public Parameter getParameter(String name) {
        mavmi.parameters_management_system.common.dto.server.inner.Value value = mavmi.parameters_management_system.common.dto.server.inner.Value
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
            ResponseEntity<GetParameterRs> responseEntity = restTemplate.postForEntity(baseUrl + getPropertyEndpoint, httpEntity, GetParameterRs.class);
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

    public void registerParameters(List<Parameter> parameters) {
        List<mavmi.parameters_management_system.common.dto.server.inner.Value> values = parameters
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
            restTemplate.postForEntity(baseUrl + registerPropertiesEndpoint, httpEntity, String.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

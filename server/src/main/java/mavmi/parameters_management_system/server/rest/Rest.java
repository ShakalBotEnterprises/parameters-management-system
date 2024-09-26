package mavmi.parameters_management_system.server.rest;

import lombok.RequiredArgsConstructor;
import mavmi.parameters_management_system.common.dto.server.inner.Value;
import mavmi.parameters_management_system.common.dto.server.request.GetParameterRq;
import mavmi.parameters_management_system.common.dto.server.request.RegisterParametersRq;
import mavmi.parameters_management_system.common.dto.server.response.GetParameterRs;
import mavmi.parameters_management_system.server.database.model.PmsModel;
import mavmi.parameters_management_system.server.database.repository.PmsRepository;
import mavmi.parameters_management_system.server.mapper.ParameterMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/parameters_management_system")
public class Rest {

    private final PmsRepository repository;
    private final ParameterMapper mapper;

    @PostMapping("/get_parameter")
    public ResponseEntity<GetParameterRs> getParameter(@RequestBody GetParameterRq requestBody) {
        String parameterName = requestBody.getValue().getName();
        Optional<PmsModel> pmsModelOptional = repository.findByName(parameterName);
        if (pmsModelOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }

        Value value = mapper.pmsModelToValueJson(pmsModelOptional.get());
        GetParameterRs responseBody = GetParameterRs.builder()
                .value(value)
                .build();

        return new ResponseEntity<>(
                responseBody,
                HttpStatusCode.valueOf(HttpStatus.OK.value())
        );
    }

    @PostMapping("/register_properties")
    public ResponseEntity<Void> registerParameters(@RequestBody RegisterParametersRq requestBody) {
        List<Value> values = requestBody.getValues();
        for (Value value : values) {
            PmsModel model = mapper.valueJsonToPmsModel(value);
            Optional<PmsModel> pmsModelOptional = repository.findByName(value.getName());
            if (pmsModelOptional.isEmpty()) {
                repository.save(model);
            }
        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}

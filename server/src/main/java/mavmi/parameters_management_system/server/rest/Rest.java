package mavmi.parameters_management_system.server.rest;

import lombok.RequiredArgsConstructor;
import mavmi.parameters_management_system.common.dto.server.request.GetParameterRq;
import mavmi.parameters_management_system.common.dto.server.request.RegisterParametersRq;
import mavmi.parameters_management_system.common.dto.server.request.UpdateParameterRq;
import mavmi.parameters_management_system.common.dto.server.request.inner.Value;
import mavmi.parameters_management_system.common.dto.server.response.GetAllParametersRs;
import mavmi.parameters_management_system.common.dto.server.response.GetParameterRs;
import mavmi.parameters_management_system.common.utils.Utils;
import mavmi.parameters_management_system.server.database.model.PmsModel;
import mavmi.parameters_management_system.server.database.repository.PmsRepository;
import mavmi.parameters_management_system.server.mapper.ParameterMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/get_all_parameters")
    public ResponseEntity<GetAllParametersRs> getAllParameters() {
        List<Value> parametersList = repository.findAll()
                .stream()
                .map(mapper::pmsModelToValueJson)
                .collect(Collectors.toList());

        GetAllParametersRs responseBody = GetAllParametersRs.builder()
                .parameters(parametersList)
                .build();

        return new ResponseEntity<>(
                responseBody,
                HttpStatusCode.valueOf(HttpStatus.OK.value())
        );
    }

    @PostMapping("/register_parameters")
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

    @PostMapping("/update_parameter")
    public ResponseEntity<Void> updateParameter(@RequestBody UpdateParameterRq requestBody) {
        Value value = requestBody.getValue();

        Optional<PmsModel> optional = repository.findByName(value.getName());
        if (optional.isEmpty()) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        }

        PmsModel model = optional.get();
        if (model.getType() != value.getType() || !Utils.verifyProperty(value.getValue(), value.getType())) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
        }

        model = mapper.valueJsonToPmsModel(value);
        repository.updateByName(model);

        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }
}

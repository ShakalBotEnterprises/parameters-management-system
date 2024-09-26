package mavmi.parameters_management_system.server.database.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import mavmi.parameters_management_system.common.parameter.api.PARAMETER_TYPE;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parameters_management_system", schema = "common")
public class PmsModel {
    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private String value;
    @Column(name = "type")
    private PARAMETER_TYPE type;
}

package mavmi.parameters_management_system.server.database.repository;

import jakarta.transaction.Transactional;
import mavmi.parameters_management_system.server.database.model.PmsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface PmsRepository extends JpaRepository<PmsModel, Long> {

    @Query(
            value = "select * from common.parameters_management_system where " +
                    "name = :name",
            nativeQuery = true
    )
    Optional<PmsModel> findByName(@Param("name") String name);
}

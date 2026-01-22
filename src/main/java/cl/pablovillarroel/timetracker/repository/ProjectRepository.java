package cl.pablovillarroel.timetracker.repository;

import cl.pablovillarroel.timetracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}

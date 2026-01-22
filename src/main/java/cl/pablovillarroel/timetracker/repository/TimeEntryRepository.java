package cl.pablovillarroel.timetracker.repository;

import cl.pablovillarroel.timetracker.model.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long> {

    Optional<TimeEntry> findFirstByUser_IdAndEndTimeIsNullOrderByStartTimeDesc(Long userId);

    @Query("SELECT te FROM TimeEntry te JOIN te.task t JOIN t.project p WHERE p.id = :projectId AND te.startTime BETWEEN :startDate AND :endDate ORDER BY te.startTime DESC")
    List<TimeEntry> findByProjectIdAndDateRange(@Param("projectId") Long projectId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}

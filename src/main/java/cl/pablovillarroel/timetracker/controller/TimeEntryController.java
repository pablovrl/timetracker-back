package cl.pablovillarroel.timetracker.controller;

import cl.pablovillarroel.timetracker.dto.StartTimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryResponse;
import cl.pablovillarroel.timetracker.service.TimeEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @GetMapping
    public ResponseEntity<List<TimeEntryResponse>> getMyTimeEntries(Authentication authentication) {
        String email = authentication.getName();
        List<TimeEntryResponse> response = timeEntryService.getMyTimeEntries(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TimeEntryResponse>> getTimeEntriesByTaskId(@PathVariable Long taskId, Authentication authentication) {
        String email = authentication.getName();
        List<TimeEntryResponse> response = timeEntryService.getTimeEntriesByTaskId(taskId, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}/range")
    public ResponseEntity<List<TimeEntryResponse>> getTimeEntriesByProjectIdAndDateRange(
            @PathVariable Long projectId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            Authentication authentication) {
        String email = authentication.getName();
        List<TimeEntryResponse> response = timeEntryService.getTimeEntriesByProjectIdAndDateRange(projectId, startDate, endDate, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryResponse> getTimeEntryById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.getTimeEntryById(id, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    public ResponseEntity<TimeEntryResponse> startTimeEntry(@Valid @RequestBody StartTimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.startTimeEntry(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/stop")
    public ResponseEntity<TimeEntryResponse> stopTimeEntry(Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.stopTimeEntry(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TimeEntryResponse> createTimeEntry(@Valid @RequestBody TimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.createTimeEntry(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntryResponse> updateTimeEntry(@PathVariable Long id, @Valid @RequestBody TimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.updateTimeEntry(id, request, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        timeEntryService.deleteTimeEntry(id, email);
        return ResponseEntity.noContent().build();
    }
}

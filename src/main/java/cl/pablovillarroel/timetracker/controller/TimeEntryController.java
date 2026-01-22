package cl.pablovillarroel.timetracker.controller;

import cl.pablovillarroel.timetracker.dto.StartTimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryResponse;
import cl.pablovillarroel.timetracker.service.TimeEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Time Entries", description = "Time entry management endpoints")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @GetMapping
    @Operation(summary = "Get my time entries", description = "Retrieve all time entries for the authenticated user")
    public ResponseEntity<List<TimeEntryResponse>> getMyTimeEntries(Authentication authentication) {
        String email = authentication.getName();
        List<TimeEntryResponse> response = timeEntryService.getMyTimeEntries(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get time entries by task", description = "Retrieve all time entries for a specific task")
    public ResponseEntity<List<TimeEntryResponse>> getTimeEntriesByTaskId(@PathVariable Long taskId, Authentication authentication) {
        String email = authentication.getName();
        List<TimeEntryResponse> response = timeEntryService.getTimeEntriesByTaskId(taskId, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}/range")
    @Operation(summary = "Get time entries by project and date range", description = "Retrieve time entries for a project within a specific date range")
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
    @Operation(summary = "Get time entry by ID", description = "Retrieve a specific time entry by its ID")
    public ResponseEntity<TimeEntryResponse> getTimeEntryById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.getTimeEntryById(id, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    @Operation(summary = "Start time entry", description = "Start a new time entry for the authenticated user")
    public ResponseEntity<TimeEntryResponse> startTimeEntry(@Valid @RequestBody StartTimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.startTimeEntry(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/stop")
    @Operation(summary = "Stop time entry", description = "Stop the currently running time entry for the authenticated user")
    public ResponseEntity<TimeEntryResponse> stopTimeEntry(Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.stopTimeEntry(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create time entry", description = "Create a new manual time entry")
    public ResponseEntity<TimeEntryResponse> createTimeEntry(@Valid @RequestBody TimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.createTimeEntry(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update time entry", description = "Update an existing time entry")
    public ResponseEntity<TimeEntryResponse> updateTimeEntry(@PathVariable Long id, @Valid @RequestBody TimeEntryRequest request, Authentication authentication) {
        String email = authentication.getName();
        TimeEntryResponse response = timeEntryService.updateTimeEntry(id, request, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete time entry", description = "Delete a time entry by its ID")
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        timeEntryService.deleteTimeEntry(id, email);
        return ResponseEntity.noContent().build();
    }
}

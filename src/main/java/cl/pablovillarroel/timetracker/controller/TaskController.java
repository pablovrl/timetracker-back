package cl.pablovillarroel.timetracker.controller;

import cl.pablovillarroel.timetracker.dto.TaskRequest;
import cl.pablovillarroel.timetracker.dto.TaskResponse;
import cl.pablovillarroel.timetracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProjectId(@PathVariable Long projectId, Authentication authentication) {
        String email = authentication.getName();
        List<TaskResponse> response = taskService.getTasksByProjectId(projectId, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        TaskResponse response = taskService.getTaskById(id, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request, Authentication authentication) {
        String email = authentication.getName();
        TaskResponse response = taskService.createTask(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request, Authentication authentication) {
        String email = authentication.getName();
        TaskResponse response = taskService.updateTask(id, request, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        taskService.deleteTask(id, email);
        return ResponseEntity.noContent().build();
    }
}

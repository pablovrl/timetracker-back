package cl.pablovillarroel.timetracker.controller;

import cl.pablovillarroel.timetracker.dto.ProjectRequest;
import cl.pablovillarroel.timetracker.dto.ProjectResponse;
import cl.pablovillarroel.timetracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(Authentication authentication) {
        String email = authentication.getName();
        List<ProjectResponse> response = projectService.getProjectsByUserEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        ProjectResponse response = projectService.getProjectById(id, email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(Authentication authentication, @Valid @RequestBody ProjectRequest request) {
        String email = authentication.getName();
        ProjectResponse response = projectService.createProject(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest request, Authentication authentication) {
        String email = authentication.getName();
        ProjectResponse response = projectService.updateProject(id, request, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        projectService.deleteProject(id, email);
        return ResponseEntity.noContent().build();
    }
}

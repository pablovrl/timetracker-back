package cl.pablovillarroel.timetracker.service;

import cl.pablovillarroel.timetracker.dto.ProjectRequest;
import cl.pablovillarroel.timetracker.dto.ProjectResponse;
import cl.pablovillarroel.timetracker.exception.ResourceNotFoundException;
import cl.pablovillarroel.timetracker.model.Project;
import cl.pablovillarroel.timetracker.model.User;
import cl.pablovillarroel.timetracker.repository.ProjectRepository;
import cl.pablovillarroel.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<ProjectResponse> getProjectsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .map(this::mapToResponse)
                .toList();
    }

    public ProjectResponse getProjectById(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id);
        }

        return mapToResponse(project);
    }

    @Transactional
    public ProjectResponse createProject(String email, ProjectRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = Project.builder()
                .user(user)
                .name(request.getName())
                .hourlyCost(request.getHourlyCost())
                .build();

        Project savedProject = projectRepository.save(project);
        return mapToResponse(savedProject);
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id);
        }

        project.setName(request.getName());
        project.setHourlyCost(request.getHourlyCost());

        Project updatedProject = projectRepository.save(project);
        return mapToResponse(updatedProject);
    }

    @Transactional
    public void deleteProject(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + id);
        }

        projectRepository.deleteById(id);
    }

    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .userId(project.getUser().getId())
                .name(project.getName())
                .hourlyCost(project.getHourlyCost())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}

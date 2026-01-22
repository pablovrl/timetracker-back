package cl.pablovillarroel.timetracker.service;

import cl.pablovillarroel.timetracker.dto.TaskRequest;
import cl.pablovillarroel.timetracker.dto.TaskResponse;
import cl.pablovillarroel.timetracker.exception.ResourceNotFoundException;
import cl.pablovillarroel.timetracker.model.Project;
import cl.pablovillarroel.timetracker.model.Task;
import cl.pablovillarroel.timetracker.model.User;
import cl.pablovillarroel.timetracker.repository.ProjectRepository;
import cl.pablovillarroel.timetracker.repository.TaskRepository;
import cl.pablovillarroel.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<TaskResponse> getTasksByProjectId(Long projectId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + projectId));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + projectId);
        }

        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .filter(t -> t.getProject().getId().equals(projectId))
                .map(this::mapToResponse)
                .toList();
    }

    public TaskResponse getTaskById(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id);
        }

        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse createTask(String email, TaskRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + request.getProjectId()));

        if (!project.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("PROJECT_NOT_FOUND", "Project not found with id: " + request.getProjectId());
        }

        Task task = Task.builder()
                .project(project)
                .name(request.getName())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id);
        }

        task.setName(request.getName());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + id);
        }

        taskRepository.deleteById(id);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .projectId(task.getProject().getId())
                .name(task.getName())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}

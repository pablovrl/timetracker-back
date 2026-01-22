package cl.pablovillarroel.timetracker.service;

import cl.pablovillarroel.timetracker.dto.StartTimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryRequest;
import cl.pablovillarroel.timetracker.dto.TimeEntryResponse;
import cl.pablovillarroel.timetracker.exception.BusinessException;
import cl.pablovillarroel.timetracker.exception.ResourceNotFoundException;
import cl.pablovillarroel.timetracker.model.Task;
import cl.pablovillarroel.timetracker.model.TimeEntry;
import cl.pablovillarroel.timetracker.model.User;
import cl.pablovillarroel.timetracker.repository.TaskRepository;
import cl.pablovillarroel.timetracker.repository.TimeEntryRepository;
import cl.pablovillarroel.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TimeEntryResponse startTimeEntry(String email, StartTimeEntryRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId()));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId());
        }

        if (timeEntryRepository.findFirstByUser_IdAndEndTimeIsNullOrderByStartTimeDesc(user.getId()).isPresent()) {
            throw new BusinessException("ACTIVE_TIME_ENTRY_EXISTS", "You already have an active time entry");
        }

        TimeEntry timeEntry = TimeEntry.builder()
                .task(task)
                .user(user)
                .startTime(LocalDateTime.now())
                .build();

        TimeEntry savedTimeEntry = timeEntryRepository.save(timeEntry);
        return mapToResponse(savedTimeEntry);
    }

    @Transactional
    public TimeEntryResponse stopTimeEntry(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        TimeEntry timeEntry = timeEntryRepository.findFirstByUser_IdAndEndTimeIsNullOrderByStartTimeDesc(user.getId())
                .orElseThrow(() -> new BusinessException("NO_ACTIVE_TIME_ENTRY", "No active time entry found"));

        timeEntry.setEndTime(LocalDateTime.now());

        long seconds = java.time.Duration.between(timeEntry.getStartTime(), timeEntry.getEndTime()).toSeconds();
        timeEntry.setDuration(seconds);

        Task task = timeEntry.getTask();
        if (task.getProject().getHourlyCost() != null) {
            BigDecimal hourlyCost = task.getProject().getHourlyCost();
            BigDecimal hours = BigDecimal.valueOf(seconds).divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);
            BigDecimal cost = hourlyCost.multiply(hours);
            timeEntry.setCost(cost);
        }

        TimeEntry updatedTimeEntry = timeEntryRepository.save(timeEntry);
        return mapToResponse(updatedTimeEntry);
    }

    public List<TimeEntryResponse> getTimeEntriesByTaskId(Long taskId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + taskId));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + taskId);
        }

        List<TimeEntry> timeEntries = timeEntryRepository.findAll();
        return timeEntries.stream()
                .filter(t -> t.getTask().getId().equals(taskId))
                .map(this::mapToResponse)
                .toList();
    }

    public List<TimeEntryResponse> getMyTimeEntries(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        List<TimeEntry> timeEntries = timeEntryRepository.findAll();
        return timeEntries.stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .map(this::mapToResponse)
                .toList();
    }

    public List<TimeEntryResponse> getTimeEntriesByProjectIdAndDateRange(Long projectId, LocalDateTime startDate, LocalDateTime endDate, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + projectId));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + projectId);
        }

        List<TimeEntry> timeEntries = timeEntryRepository.findByProjectIdAndDateRange(projectId, startDate, endDate);
        return timeEntries.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TimeEntryResponse getTimeEntryById(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        TimeEntry timeEntry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id));

        if (!timeEntry.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id);
        }

        return mapToResponse(timeEntry);
    }

    @Transactional
    public TimeEntryResponse createTimeEntry(String email, TimeEntryRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId()));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId());
        }

        TimeEntry timeEntry = TimeEntry.builder()
                .task(task)
                .user(user)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getDuration())
                .cost(request.getCost())
                .build();

        TimeEntry savedTimeEntry = timeEntryRepository.save(timeEntry);
        return mapToResponse(savedTimeEntry);
    }

    @Transactional
    public TimeEntryResponse updateTimeEntry(Long id, TimeEntryRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        TimeEntry timeEntry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id));

        if (!timeEntry.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id);
        }

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId()));

        if (!task.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TASK_NOT_FOUND", "Task not found with id: " + request.getTaskId());
        }

        timeEntry.setTask(task);
        timeEntry.setStartTime(request.getStartTime());
        timeEntry.setEndTime(request.getEndTime());
        timeEntry.setDuration(request.getDuration());
        timeEntry.setCost(request.getCost());

        TimeEntry updatedTimeEntry = timeEntryRepository.save(timeEntry);
        return mapToResponse(updatedTimeEntry);
    }

    @Transactional
    public void deleteTimeEntry(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "User not found with email: " + email));

        TimeEntry timeEntry = timeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id));

        if (!timeEntry.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("TIME_ENTRY_NOT_FOUND", "Time entry not found with id: " + id);
        }

        timeEntryRepository.deleteById(id);
    }

    private TimeEntryResponse mapToResponse(TimeEntry timeEntry) {
        return TimeEntryResponse.builder()
                .id(timeEntry.getId())
                .taskId(timeEntry.getTask().getId())
                .userId(timeEntry.getUser().getId())
                .startTime(timeEntry.getStartTime())
                .endTime(timeEntry.getEndTime())
                .duration(timeEntry.getDuration())
                .cost(timeEntry.getCost())
                .build();
    }
}

package cl.pablovillarroel.timetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private Long id;
    private Long projectId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

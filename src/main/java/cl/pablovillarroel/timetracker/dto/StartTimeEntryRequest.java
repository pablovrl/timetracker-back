package cl.pablovillarroel.timetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartTimeEntryRequest {

    @NotNull(message = "Task ID is required")
    private Long taskId;
}

package cl.pablovillarroel.timetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeEntryResponse {

    private Long id;
    private Long taskId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;
    private BigDecimal cost;
}

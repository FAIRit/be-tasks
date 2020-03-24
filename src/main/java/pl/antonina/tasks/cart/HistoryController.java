package pl.antonina.tasks.cart;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{childId}")
    public List<HistoryView> getByChildId(@PathVariable long childId) {
        return historyService.getByChildId(childId);
    }

    @DeleteMapping("/{historyId}")
    public void deleteHistory(@PathVariable long historyId) {
        historyService.deleteHistory(historyId);
    }
}
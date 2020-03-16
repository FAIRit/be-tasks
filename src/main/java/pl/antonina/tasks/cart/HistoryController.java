package pl.antonina.tasks.cart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/history")
@Controller
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<HistoryView> getByChildId(@RequestParam long childId) {
        return historyService.getByChildId(childId);
    }

    @DeleteMapping("/{historyId}")
    public void deleteHistory(@PathVariable long historyId) {
        historyService.deleteHistory(historyId);
    }
}
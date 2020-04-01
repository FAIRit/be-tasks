package pl.antonina.tasks.cart;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{childId}")
    public List<HistoryView> getByChildId(@ApiIgnore Principal parentPrincipal, @PathVariable long childId) {
        return historyService.getByChildId(parentPrincipal, childId);
    }
}
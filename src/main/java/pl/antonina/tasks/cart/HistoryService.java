package pl.antonina.tasks.cart;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final ChildRepository childRepository;

    public HistoryService(HistoryRepository historyRepository, HistoryMapper historyMapper, ChildRepository childRepository) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.childRepository = childRepository;
    }

    List<HistoryView> getByChildId(long childId) {
        List<History> historyList = historyRepository.findByChildId(childId);
        return historyList.stream()
                .map(historyMapper::mapHistoryView)
                .collect(Collectors.toList());
    }

    void addHistory(long childId, HistoryData historyData) {
        Child child = childRepository.findById(childId).orElseThrow();
        History history = new History();
        mapHistory(historyData, history);
        history.setChild(child);
        historyRepository.save(history);
    }

    void deleteHistory(long id) {
        historyRepository.deleteById(id);
    }

    private void mapHistory(HistoryData historyData, History history) {
        history.setMessage(historyData.getMessage());
        history.setModificationDate(historyData.getModificationDate());
        history.setQuantity(historyData.getQuantity());
    }
}




package pl.antonina.tasks.allegro.offer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/allegro")
public class AllegroOfferController {

    private final AllegroOfferService allegroOfferService;

    public AllegroOfferController(AllegroOfferService allegroOfferService) {
        this.allegroOfferService = allegroOfferService;
    }

    @GetMapping("/offers")
    public List<OfferView> getOffers() {
        return allegroOfferService.getOffers();
    }
}
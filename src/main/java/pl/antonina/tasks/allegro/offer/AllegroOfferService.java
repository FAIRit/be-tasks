package pl.antonina.tasks.allegro.offer;

import java.io.IOException;
import java.util.List;

public interface AllegroOfferService {

    List<OfferView> getOffers() throws IOException;
}
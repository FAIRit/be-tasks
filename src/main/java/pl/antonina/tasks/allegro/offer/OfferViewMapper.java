package pl.antonina.tasks.allegro.offer;

import org.springframework.stereotype.Component;
import pl.antonina.tasks.allegro.offer.api.Items;
import pl.antonina.tasks.allegro.offer.api.Offer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OfferViewMapper {

    List<OfferView> mapOfferViews(Items items) {
        return Stream.concat(items.getPromoted().stream(), items.getRegular().stream())
                .map(this::mapOffer)
                .collect(Collectors.toList());
    }

    private OfferView mapOffer(Offer offer) {
        OfferView offerView = new OfferView();
        offerView.setName(offer.getName());
        offerView.setPrice(offer.getSellingMode().getPrice().getAmount());
        offerView.setUrl(offer.getImages().get(0).getUrl());
        return offerView;
    }
}
package ch.bbw.obelix.webshop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

import ch.bbw.obelix.quarry.api.DecorativenessDto;
import ch.bbw.obelix.quarry.api.MenhirDto;
import ch.bbw.obelix.quarry.api.QuarryApi;
import ch.bbw.obelix.webshop.dto.BasketDto;

import io.micrometer.observation.annotation.Observed;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.StandardException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Note that Obelix is definitely not multitasking-capable.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {

	private final QuarryApi quarryWebclient;

	private BasketDto basket;

	static <T> List<T> append(List<T> immutableList, T element) {
		var tmpList = new ArrayList<>(immutableList);
		tmpList.add(element);
		return Collections.unmodifiableList(tmpList);
	}

	public List<MenhirDto> getAllMenhirs() {
		return quarryWebclient.getAllMenhirs();
	}

	public MenhirDto getMenhirById(UUID menhirId) {
		try {
			return quarryWebclient.getMenhirById(menhirId);
		} catch (WebClientResponseException.BadRequest e) {
			throw new UnknownMenhirException("unknown menhir with id " + menhirId, e);
		}
	}

	@Observed(name = "obelix.basket.offer", contextualName = "offer-basket-item")
	public BasketDto offer(@NonNull BasketDto.BasketItem basketItem) {
		basket = basket.withItems(append(basket.items(), basketItem));
		return basket;
	}

	@PostConstruct
	public void leave() {
		basket = BasketDto.empty();
	}

	public boolean isGoodOffer(DecorativenessDto decorativeness) {
		var stoneWorth = decorativeness.ordinal();

		var basketWorth = basket.items()
			.stream().mapToInt(x -> switch (x.name().toLowerCase(Locale.ROOT)) {
				case "boar" -> 5; // oh boy, oh boy!
				case "honey" -> 2;
				case "magic potion" -> 0; // not allowed to drink this!
				default -> 1; // everything is worth something
			} * x.count()).sum();

		log.info("basket worth {} vs menhir worth {} ({})", basketWorth, decorativeness, stoneWorth);

		return basketWorth >= stoneWorth;
	}

	@Observed(name = "obelix.basket.exchange", contextualName = "exchange-basket-for-menhir")
	public void exchange(UUID menhirId) {
		var menhir = getMenhirById(menhirId);

		var decorativeness = menhir.decorativeness();

		if (!isGoodOffer(decorativeness)) {
			throw new BadOfferException("Bad Offer: That won't even feed Idefix!");
		}

		quarryWebclient.deleteById(menhirId);

		leave();
	}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class BadOfferException extends RuntimeException {}

	@StandardException
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public static class UnknownMenhirException extends RuntimeException {}
}
package ch.bbw.obelix.quarry;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ch.bbw.obelix.webshop.dto.MenhirDto;

@RestController
@RequiredArgsConstructor
public class QuarryController {

	private final ObelixWebshopService obelixWebshopService;

	private final QuarryRepository menhirRepository;

    @GetMapping("/api/menhirs")
	public List<MenhirDto> getAllMenhirs() {
		return menhirRepository.findAll()
			.stream().map(MenhirEntity::toDto).toList();
	}
}
package lv.mintos.demo.DemoApp;

import lv.mintos.demo.DemoApp.dto.TransactionRequestDTO;
import lv.mintos.demo.DemoApp.model.Account;
import lv.mintos.demo.DemoApp.model.Client;
import lv.mintos.demo.DemoApp.model.repo.AccountRepository;
import lv.mintos.demo.DemoApp.model.repo.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AccountRepository accountRepository;

	private Long sourceAccountId;
	private Long destinationAccountId;

	@BeforeEach
	public void setup() {
		Client client = new Client();
		client.setName("Test Client");
		client = clientRepository.save(client);

		Account source = new Account();
		source.setClient(client);
		source.setCurrency("USD");
		source.setBalance(new BigDecimal("500.00"));
		accountRepository.save(source);
		sourceAccountId = source.getId();

		Account dest = new Account();
		dest.setClient(client);
		dest.setCurrency("USD");
		dest.setBalance(new BigDecimal("100.00"));
		accountRepository.save(dest);
		destinationAccountId = dest.getId();
	}

	@Test
	public void testTransactionSuccess() {
		TransactionRequestDTO request = new TransactionRequestDTO(
				sourceAccountId,
				destinationAccountId,
				new BigDecimal("50.00"),
				"Test transfer"
		);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TransactionRequestDTO> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(
				"http://localhost:" + port + "/transaction",
				entity,
				String.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}






}

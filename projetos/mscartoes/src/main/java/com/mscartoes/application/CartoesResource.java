package com.mscartoes.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mscartoes.application.representation.CartaoSaveRequest;
import com.mscartoes.application.representation.CartoesPorClienteResponse;
import com.mscartoes.domain.Cartao;
import com.mscartoes.domain.ClienteCartao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("cartoes")
@Slf4j
@RequiredArgsConstructor
public class CartoesResource {

	private final CartaoService cartaoService;
	private final ClienteCartaoService clienteCartaoService;
	
	@GetMapping("/status")
	public String status() {
		log.info("Obtendo status!");
		return "ok";
	}
	
	@PostMapping
	public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request) {
		Cartao cartao = request.toModel();
		cartaoService.save(cartao);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(params = "renda")
	public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
		List<Cartao> lista = cartaoService.getCartoesRendaMenorIgual(renda);
		
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(params = "cpf")
	public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
		List<ClienteCartao> cartoes = clienteCartaoService.listCartoesByCpf(cpf);
		List<CartoesPorClienteResponse> resultList = cartoes.stream().map(CartoesPorClienteResponse::fromModel).collect(Collectors.toList());
		
		return ResponseEntity.ok(resultList); 
	}
	
}

package com.msavaliadorcredito.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import com.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import com.msavaliadorcredito.domain.model.Cartao;
import com.msavaliadorcredito.domain.model.CartaoAprovado;
import com.msavaliadorcredito.domain.model.CartaoCliente;
import com.msavaliadorcredito.domain.model.DadosCliente;
import com.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import com.msavaliadorcredito.domain.model.SituacaoCliente;
import com.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.msavaliadorcredito.infra.clients.ClienteResourceClient;

import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

	private final ClienteResourceClient clientesClient;
	private final CartoesResourceClient cartoesClient;
	
	public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
		try {
			ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
			ResponseEntity<List<CartaoCliente>> cartoesCliente = cartoesClient.getCartoesByCliente(cpf);
			
			return SituacaoCliente
					.builder()
					.cliente(dadosClienteResponse.getBody())
					.cartoes(cartoesCliente.getBody())
					.build();
		} catch (FeignClientException e) {
			int status = e.status();
			if (HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
		}
	}
	
	public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
		try {
			ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
			ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAte(renda);
			
			List<Cartao> cartoes = cartoesResponse.getBody();
			List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {
				
				DadosCliente dadosCliente = dadosClienteResponse.getBody();
				
				BigDecimal limiteBasico = cartao.getLimiteBasico();
				BigDecimal rendBd = BigDecimal.valueOf(renda);
				BigDecimal idadeBd = BigDecimal.valueOf(dadosCliente.getIdade());
				
				BigDecimal fator = idadeBd.divide(BigDecimal.valueOf(10));
				BigDecimal limiteAprovado = fator.multiply(limiteBasico);
				
				CartaoAprovado aprovado = new CartaoAprovado();
				aprovado.setCartao(cartao.getNome());
				aprovado.setBandeira(cartao.getBandeira());
				aprovado.setLimiteAprovado(limiteAprovado);
				
				return aprovado;
			}).collect(Collectors.toList());
			
			return new RetornoAvaliacaoCliente(listaCartoesAprovados);
			
		}
		catch (FeignClientException e) {
			int status = e.status();
			if (HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
		}
	}
	
}

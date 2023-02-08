package com.msavaliadorcredito.domain.model;

import java.util.List;

import lombok.Data;

@Data
public class RetornoAvaliacaoCliente {

	private List<CartaoAprovado> cartoes;
	
}

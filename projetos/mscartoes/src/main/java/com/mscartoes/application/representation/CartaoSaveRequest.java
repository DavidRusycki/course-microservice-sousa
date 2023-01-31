package com.mscartoes.application.representation;

import java.math.BigDecimal;

import com.mscartoes.domain.Cartao;
import com.mscartoes.enums.BandeiraCartao;

import lombok.Data;

@Data
public class CartaoSaveRequest {

	private String nome;
	private String bandeira;
	private BigDecimal renda;
	private BigDecimal limite;
	
	public Cartao toModel() {
		return new Cartao(nome, BandeiraCartao.valueOf(bandeira), renda, limite);
	}
	
}

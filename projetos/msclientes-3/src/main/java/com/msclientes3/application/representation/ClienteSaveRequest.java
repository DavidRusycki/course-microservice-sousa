package com.msclientes3.application.representation;

import com.msclientes3.domain.Cliente;

import lombok.Data;

@Data
public class ClienteSaveRequest {

	private String cpf;
	private String nome;
	private Integer idade;
	
	public Cliente toModel() {
		return new Cliente(cpf, nome, idade);
	}
	
}

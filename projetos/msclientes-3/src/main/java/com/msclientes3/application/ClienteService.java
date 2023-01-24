package com.msclientes3.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.msclientes3.domain.Cliente;
import com.msclientes3.infra.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

	@Autowired
	private final ClienteRepository repository = null;
	
	@Transactional
	public Cliente save(Cliente cliente) {
		return repository.save(cliente);
	}
	
	public Optional<Cliente> getByCpf(String cpf) {
		return repository.findByCpf(cpf);
	}
	
}

package com.api.vendas.repository;

import com.api.vendas.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByCpf(String cpf);
}

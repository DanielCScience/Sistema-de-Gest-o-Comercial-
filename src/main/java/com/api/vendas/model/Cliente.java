package com.api.vendas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcliente;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String telefone;
    private String endereco;

    @Column(nullable = false, updatable = false)
    private LocalDateTime datacadastro;

    @PrePersist
    public void prePersist() {
        if (this.datacadastro == null) {
            this.datacadastro = LocalDateTime.now();
        }
    }
}

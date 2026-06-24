package com.api.vendas.security;

import com.api.vendas.model.Cliente;
import com.api.vendas.model.Funcionario;
import com.api.vendas.repository.ClienteRepository;
import com.api.vendas.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<Funcionario> funcionarioOpt = funcionarioRepository.findByEmail(email);
        if (funcionarioOpt.isPresent()) {
            Funcionario f = funcionarioOpt.get();
            String role = "ROLE_" + f.getPerfil().name();
            return new User(
                    f.getEmail(),
                    f.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        }

        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        if (clienteOpt.isPresent()) {
            Cliente c = clienteOpt.get();
            return new User(
                    c.getEmail(),
                    c.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"))
            );
        }

        throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email);
    }
}

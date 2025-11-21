package com.example.sea.business; // <-- Pacote 'business'

// Importa as camadas que este controlador vai usar
import com.example.sea.data.IRepositorioSala;
import com.example.sea.data.RepositorioSala; // A implementação concreta
import com.example.sea.model.Sala;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;

import java.util.List;


public class ControladorSala implements IControladorSala {

    private IRepositorioSala repositorioSala;

    public ControladorSala() {
        // Instancia a implementação concreta do repositório
        this.repositorioSala = new RepositorioSala();
    }


    @Override
    public void cadastrar(Sala sala) throws SalaJaExisteException, CampoVazioException, Exception {
        // --- REGRA DE NEGÓCIO 1: Validação de Objeto Nulo ---
        if (sala == null) {
            throw new IllegalArgumentException("O objeto 'Sala' não pode ser nulo.");
        }
        
        // --- REGRA DE NEGÓCIO 2: Validação de Campos Vazios ---
        if (sala.getNome() == null || sala.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }

        // --- REGRA DE NEGÓCIO 3: Validação de Valores (Lógica) ---
        if (sala.getCapacidade() <= 0) {
            throw new Exception("A capacidade da sala deve ser maior que zero.");
        }
        
        // Se todas as regras de negócio passaram, chama a camada 'data'
        this.repositorioSala.salvar(sala);
    }

    @Override
    public Sala buscar(String nome) throws SalaNaoEncontradaException, CampoVazioException {
        // --- REGRA DE NEGÓCIO: Validação de Campos ---
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        
        return this.repositorioSala.buscarPorNome(nome);
    }

    @Override
    public List<Sala> listar() {
        return this.repositorioSala.listarTodas();
    }

    @Override
    public void atualizar(Sala sala) throws SalaNaoEncontradaException, CampoVazioException, Exception {
        if (sala == null) {
            throw new IllegalArgumentException("O objeto 'Sala' não pode ser nulo.");
        }
        if (sala.getNome() == null || sala.getNome().trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }
        if (sala.getCapacidade() <= 0) {
            throw new Exception("A capacidade da sala deve ser maior que zero.");
        }

        // Se passou nas validações, chama a camada 'data'
        this.repositorioSala.atualizar(sala);
    }

    @Override
    public void remover(String nome) throws SalaNaoEncontradaException, CampoVazioException {
        // Validação
        if (nome == null || nome.trim().isEmpty()) {
            throw new CampoVazioException("Nome");
        }

        // Repassa a chamada
        this.repositorioSala.deletar(nome);
    }
}
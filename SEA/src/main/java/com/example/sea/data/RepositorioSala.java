package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Sala;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;

import java.util.ArrayList;
import java.util.List;


public class RepositorioSala implements IRepositorioSala {

    // A lista em memória
    private List<Sala> salas;
    
    // O nome do ficheiro .dat
    private static final String NOME_ARQUIVO = "RepoSalas.dat";

    public RepositorioSala() {
        this.salas = new ArrayList<>();
        this.carregarDados(); 
    }


    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoSalas.dat' não encontrado. A começar com lista vazia.");
            return; 
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            this.salas = (ArrayList<Sala>) ois.readObject();

        } catch (EOFException e) {
            // Fim do ficheiro, normal.
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados das salas: " + e.getMessage());
            this.salas = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(this.salas);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das salas: " + e.getMessage());
        }
    }
    

    @Override
    public void salvar(Sala sala) throws SalaJaExisteException {
        if (sala == null) return;
        
        try {
            // Tenta buscar para ver se já existe (usando o nome como ID)
            buscarPorNome(sala.getNome());
            // Se encontrou, lança a exceção
            throw new SalaJaExisteException(sala.getNome());
            
        } catch (SalaNaoEncontradaException e) {
            // Se NÃO encontrou , ele cai aqui.
            this.salas.add(sala); // Adiciona na lista em memória
            this.salvarDados(); // Salva a lista no ficheiro .dat
            System.out.println("Sala salva com sucesso: " + sala.getNome());
        }
    }

    @Override
    public Sala buscarPorNome(String nome) throws SalaNaoEncontradaException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new SalaNaoEncontradaException("Nome nulo ou vazio");
        }
        for (Sala s : this.salas) {
            if (s.getNome().equalsIgnoreCase(nome)) {
                return s; // Encontrou
            }
        }
        // Se o loop 'for' terminar e não encontrar, lança a exceção
        throw new SalaNaoEncontradaException(nome);
    }

    @Override
    public List<Sala> listarTodas() {
        return new ArrayList<>(this.salas); // Retorna uma cópia
    }

    @Override
    public void atualizar(Sala sala) throws SalaNaoEncontradaException {
        if (sala == null) {
            throw new SalaNaoEncontradaException("Sala nula");
        }
        
        // O buscarPorNome já lança a exceção se não encontrar
        Sala existente = this.buscarPorNome(sala.getNome()); 
        
        // Se encontrou, atualiza (em memória)
        existente.setCapacidade(sala.getCapacidade());
        existente.setNumeroProjetores(sala.getNumeroProjetores());
        existente.setNumeroCaixasSom(sala.getNumeroCaixasSom());
        existente.setStatus(sala.getStatus());
        
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Sala atualizada: " + sala.getNome());
    }

    @Override
    public void deletar(String nome) throws SalaNaoEncontradaException {
        // O buscarPorNome já lança a exceção se não encontrar
        Sala paraRemover = this.buscarPorNome(nome); 
        
        this.salas.remove(paraRemover); // Remove da lista
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Sala removida: " + nome);
    }
}
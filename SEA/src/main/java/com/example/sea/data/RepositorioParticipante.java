package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe salva os dados de Participantes num ficheiro.
 */
public class RepositorioParticipante implements IRepositorioParticipante {

    // A lista em memória
    private List<Participante> participantes;
    
    // O nome do ficheiro .dat
    private static final String NOME_ARQUIVO = "RepoParticipantes.dat";

    //Construtor: Carrega os dados do ficheiro .dat para a memória.
    
    public RepositorioParticipante() {
        this.participantes = new ArrayList<>();
        this.carregarDados(); // Carrega o "banco"
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoParticipantes.dat' não encontrado. A começar com lista vazia.");
            return; 
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            this.participantes = (ArrayList<Participante>) ois.readObject();

        } catch (EOFException e) {
            // Fim do ficheiro, normal.
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados dos participantes: " + e.getMessage());
            this.participantes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(this.participantes);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados dos participantes: " + e.getMessage());
        }
    }
    

    @Override
    public void salvar(Participante participante) throws ParticipanteJaExisteException {
        if (participante == null) return;
        
        try {
            // Tenta buscar pelo CPF para ver se já existe
            buscarPorIdentificador(participante.getCpf());
            // Se encontrou, lança a exceção
            throw new ParticipanteJaExisteException(participante.getCpf());
            
        } catch (ParticipanteNaoEncontradoException e) {
            // Se NÃO encontrou, ele cai aqui.
            this.participantes.add(participante); // Adiciona na lista em memória
            this.salvarDados(); // Salva a lista no ficheiro .dat
            System.out.println("Participante salvo com sucesso: " + participante.getNome());
        }
    }

    @Override
    public Participante buscarPorIdentificador(String cpf) throws ParticipanteNaoEncontradoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new ParticipanteNaoEncontradoException("CPF nulo ou vazio");
        }
        for (Participante p : this.participantes) {
            if (p.getCpf().equals(cpf)) {
                return p; // Encontrou
            }
        }
        // Se o loop 'for' terminar e não encontrar, lança a exceção
        throw new ParticipanteNaoEncontradoException(cpf);
    }

    @Override
    public List<Participante> listarTodos() {
        return new ArrayList<>(this.participantes);
    }

    @Override
    public void atualizar(Participante participante) throws ParticipanteNaoEncontradoException {
        if (participante == null) {
            throw new ParticipanteNaoEncontradoException("Participante nulo");
        }
        
        // O buscarPorIdentificador já lança a exceção se não encontrar
        Participante existente = this.buscarPorIdentificador(participante.getCpf()); 
        
        // Se encontrou, atualiza (em memória)
        // (Nota: O CPF não é atualizável, pois é o nosso ID)
        existente.setNome(participante.getNome());
        existente.setEmail(participante.getEmail());
        existente.setInstituicao(participante.getInstituicao());
        
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Participante atualizado: " + participante.getNome());
    }

    @Override
    public void deletar(String cpf) throws ParticipanteNaoEncontradoException {
        // O buscarPorIdentificador já lança a exceção se não encontrar
        Participante paraRemover = this.buscarPorIdentificador(cpf); 
        
        this.participantes.remove(paraRemover); // Remove da lista
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Participante removido: " + cpf);
    }
}
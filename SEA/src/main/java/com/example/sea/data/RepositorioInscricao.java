package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.InscricaoJaExisteException;
import com.example.sea.exceptions.InscricaoNaoEncontradaException;

import java.util.ArrayList;
import java.util.List;

/**
 * IMPLEMENTAÇÃO com Serialização (ficheiros .dat) da interface IRepositorioInscricao.
 * Esta classe salva os dados de Inscrições num ficheiro.
 */
public class RepositorioInscricao implements IRepositorioInscricao {

    // A lista em memória
    private List<Inscricao> inscricoes;
    
    // O nome do ficheiro .dat
    private static final String NOME_ARQUIVO = "RepoInscricoes.dat";

     // Construtor: Carrega os dados do ficheiro .dat para a memória.
    public RepositorioInscricao() {
        this.inscricoes = new ArrayList<>();
        this.carregarDados(); 
    }

    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoInscricoes.dat' não encontrado. A começar com lista vazia.");
            return; 
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            this.inscricoes = (ArrayList<Inscricao>) ois.readObject();

        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados das inscrições: " + e.getMessage());
            this.inscricoes = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(this.inscricoes);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das inscrições: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Inscricao inscricao) throws InscricaoJaExisteException {
        if (inscricao == null) return;
        
        try {
            // Tenta buscar para ver se já existe
            buscar(inscricao.getParticipante(), inscricao.getPalestra());
            // Se encontrou, lança a exceção
            throw new InscricaoJaExisteException(
                inscricao.getParticipante().getNome(), 
                inscricao.getPalestra().getTitulo()
            );
            
        } catch (InscricaoNaoEncontradaException e) {
            // Se NÃO encontrou, ele cai aqui.
            this.inscricoes.add(inscricao); // Adiciona na lista em memória
            this.salvarDados(); // Salva a lista no ficheiro .dat
            System.out.println("Inscrição salva com sucesso.");
        }
    }

    @Override
    public Inscricao buscar(Participante participante, Palestra palestra) throws InscricaoNaoEncontradaException {
        if (participante == null || palestra == null) {
            throw new InscricaoNaoEncontradaException();
        }
        for (Inscricao i : this.inscricoes) {
            // A identidade única de uma inscrição é o par (participante, palestra)
            boolean mesmoParticipante = i.getParticipante().getCpf().equals(participante.getCpf());
            boolean mesmaPalestra = i.getPalestra().getTitulo().equals(palestra.getTitulo()); // (Idealmente seria por um ID)
            
            if (mesmoParticipante && mesmaPalestra) {
                return i; // Encontrou
            }
        }
        // Se o loop 'for' terminar e não encontrar, lança a exceção
        throw new InscricaoNaoEncontradaException();
    }

    @Override
    public List<Inscricao> listarTodas() {
        return new ArrayList<>(this.inscricoes); // Retorna uma cópia
    }

    @Override
    public List<Inscricao> listarPorPalestra(Palestra palestra) {
        List<Inscricao> listaFiltrada = new ArrayList<>();
        if (palestra == null) return listaFiltrada;

        for (Inscricao i : this.inscricoes) {
            // Compara pelo título (idealmente seria por ID)
            if (i.getPalestra().getTitulo().equals(palestra.getTitulo())) {
                listaFiltrada.add(i);
            }
        }
        return listaFiltrada;
    }

    @Override
    public List<Inscricao> listarPorParticipante(Participante participante) {
        List<Inscricao> listaFiltrada = new ArrayList<>();
        if (participante == null) return listaFiltrada;

        for (Inscricao i : this.inscricoes) {
            // Compara pelo CPF
            if (i.getParticipante().getCpf().equals(participante.getCpf())) {
                listaFiltrada.add(i);
            }
        }
        return listaFiltrada;
    }

    @Override
    public void atualizar(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) {
            throw new InscricaoNaoEncontradaException();
        }
        
        // O buscar já lança a exceção se não encontrar
        Inscricao existente = this.buscar(inscricao.getParticipante(), inscricao.getPalestra()); 
        
        // Se encontrou, atualiza (em memória)
        // A principal coisa a atualizar é a presença
        existente.setPresenca(inscricao.isPresenca());
        existente.setStatusConfirmacao(inscricao.getStatusConfirmacao());
        // (Geralmente não se muda o participante ou a palestra de uma inscrição)
        
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Inscrição atualizada.");
    }

    @Override
    public void deletar(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) {
            throw new InscricaoNaoEncontradaException();
        }
        
        // O buscar já lança a exceção se não encontrar
        Inscricao paraRemover = this.buscar(inscricao.getParticipante(), inscricao.getPalestra()); 
        
        this.inscricoes.remove(paraRemover); // Remove da lista
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Inscrição removida.");
    }
}
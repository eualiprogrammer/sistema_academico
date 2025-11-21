package com.example.sea.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;

import com.example.sea.model.Evento;
import com.example.sea.model.Palestra;
import com.example.sea.exceptions.PalestraJaExisteException;
import com.example.sea.exceptions.PalestraNaoEncontradaException;

import java.util.ArrayList;
import java.util.List;

/**
 * IMPLEMENTAÇÃO com Serialização (ficheiros .dat) da interface IRepositorioPalestra.
 * Esta classe salva os dados de Palestras num ficheiro.
 */
public class RepositorioPalestra implements IRepositorioPalestra {

    // A lista em memória
    private List<Palestra> palestras;
    
    // O nome do ficheiro .dat
    private static final String NOME_ARQUIVO = "RepoPalestras.dat";

    //Construtor: Carrega os dados do ficheiro .dat para a memória.
    public RepositorioPalestra() {
        this.palestras = new ArrayList<>();
        this.carregarDados(); // Carrega o "banco"
    }


    @SuppressWarnings("unchecked")
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            System.out.println("Ficheiro 'RepoPalestras.dat' não encontrado. A começar com lista vazia.");
            return; 
        }

        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            this.palestras = (ArrayList<Palestra>) ois.readObject();

        } catch (EOFException e) {
            // Fim do ficheiro, normal.
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados das palestras: " + e.getMessage());
            this.palestras = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(this.palestras);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados das palestras: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Palestra palestra) throws PalestraJaExisteException {
        if (palestra == null) return;
        
        try {
            // Tenta buscar pelo título para ver se já existe
            buscarPorTitulo(palestra.getTitulo());
            // Se encontrou, lança a exceção
            throw new PalestraJaExisteException(palestra.getTitulo());
            
        } catch (PalestraNaoEncontradaException e) {
            // Se NÃO encontrou ele cai aqui.
            this.palestras.add(palestra); // Adiciona na lista em memória
            this.salvarDados(); // Salva a lista no ficheiro .dat
            System.out.println("Palestra salva com sucesso: " + palestra.getTitulo());
        }
    }

    @Override
    public Palestra buscarPorTitulo(String titulo) throws PalestraNaoEncontradaException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new PalestraNaoEncontradaException("Título nulo ou vazio");
        }
        for (Palestra p : this.palestras) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return p; // Encontrou
            }
        }
        // Se o loop 'for' terminar e não encontrar, lança a exceção
        throw new PalestraNaoEncontradaException(titulo);
    }

    @Override
    public List<Palestra> listarTodas() {
        return new ArrayList<>(this.palestras); // Retorna uma cópia
    }

    @Override
    public List<Palestra> listarPorEvento(Evento evento) {
        List<Palestra> listaFiltrada = new ArrayList<>();
        if (evento == null) return listaFiltrada;

        for (Palestra p : this.palestras) {
            // Compara pelo nome do evento (idealmente seria por ID)
            if (p.getEvento().getNome().equals(evento.getNome())) {
                listaFiltrada.add(p);
            }
        }
        return listaFiltrada;
    }

    @Override
    public void atualizar(Palestra palestra) throws PalestraNaoEncontradaException {
        if (palestra == null) {
            throw new PalestraNaoEncontradaException("Palestra nula");
        }
        
        // O buscarPorTitulo já lança a exceção se não encontrar
        Palestra existente = this.buscarPorTitulo(palestra.getTitulo()); 
        
        // Se encontrou, atualiza (em memória)
        // (Nota: O Título não é atualizável, pois é o nosso ID)
        existente.setDescricao(palestra.getDescricao());
        existente.setDataHoraInicio(palestra.getDataHoraInicio());
        existente.setDuracaoHoras(palestra.getDuracaoHoras());
        existente.setSala(palestra.getSala());
        existente.setPalestrante(palestra.getPalestrante());
        // A lista de inscrições NÃO é gerida aqui.
        
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Palestra atualizada: " + palestra.getTitulo());
    }

    @Override
    public void deletar(String titulo) throws PalestraNaoEncontradaException {
        // O buscarPorTitulo já lança a exceção se não encontrar
        Palestra paraRemover = this.buscarPorTitulo(titulo); 
        
        this.palestras.remove(paraRemover); // Remove da lista
        this.salvarDados(); // Salva no ficheiro
        System.out.println("Palestra removida: " + titulo);
    }
}
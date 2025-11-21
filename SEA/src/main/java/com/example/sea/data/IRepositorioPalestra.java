package com.example.sea.data;

import com.example.sea.model.Palestra;
import com.example.sea.model.Evento;
import com.example.sea.exceptions.PalestraJaExisteException;
import com.example.sea.exceptions.PalestraNaoEncontradaException;
import java.util.List;

/**
 * Interface (Contrato) para o Repositório de Palestra.
 * Define as operações CRUD obrigatórias para a entidade Palestra.
 */
public interface IRepositorioPalestra {

    /**
     * Salva uma nova palestra no repositório.
     * @param palestra O objeto Palestra a ser salvo.
     * @throws PalestraJaExisteException Se uma palestra com o mesmo título já existir.
     */
    void salvar(Palestra palestra) throws PalestraJaExisteException;

    /**
     * Busca uma palestra pelo seu título (que usaremos como identificador único).
     * @param titulo O título da palestra.
     * @return O objeto Palestra encontrado.
     * @throws PalestraNaoEncontradaException Se a palestra não for encontrada.
     */
    Palestra buscarPorTitulo(String titulo) throws PalestraNaoEncontradaException;

    /**
     * Retorna uma lista com todas as palestras cadastradas.
     * @return Uma List<Palestra>.
     */
    List<Palestra> listarTodas();
    
    /**
     * Retorna todas as palestras associadas a um Evento específico.
     * @param evento O evento-pai.
     * @return Uma List<Palestra> daquele evento.
     */
    List<Palestra> listarPorEvento(Evento evento);

    /**
     * Atualiza os dados de uma palestra já existente (identificada pelo título).
     * @param palestra O objeto Palestra com os dados atualizados.
     * @throws PalestraNaoEncontradaException Se a palestra a ser atualizada não for encontrada.
     */
    void atualizar(Palestra palestra) throws PalestraNaoEncontradaException;

    /**
     * Remove uma palestra do repositório.
     * @param titulo O título da palestra a ser removida.
     * @throws PalestraNaoEncontradaException Se a palestra a ser removida não for encontrada.
     */
    void deletar(String titulo) throws PalestraNaoEncontradaException;
}
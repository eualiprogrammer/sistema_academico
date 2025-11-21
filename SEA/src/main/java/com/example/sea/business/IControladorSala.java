package com.example.sea.business;

import com.example.sea.model.Sala;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;
import com.example.sea.exceptions.CampoVazioException;
import java.util.List;

public interface IControladorSala {

    /**
     * Regras de negócio para cadastrar uma nova sala.
     * @param sala A sala a ser cadastrada.
     * @throws SalaJaExisteException Se a sala já existir.
     * @throws CampoVazioException Se o 'nome' estiver vazio.
     * @throws Exception (para outras regras de negócio, ex: capacidade <= 0)
     */
    void cadastrar(Sala sala) throws SalaJaExisteException, CampoVazioException, Exception;

    /**
     * Busca uma sala pelo seu nome.
     * @param nome O nome da sala.
     * @return A Sala encontrada.
     * @throws SalaNaoEncontradaException Se a sala não for encontrada.
     * @throws CampoVazioException Se 'nome' estiver vazio.
     */
    Sala buscar(String nome) throws SalaNaoEncontradaException, CampoVazioException;

    /**
     * Retorna todas as salas cadastradas.
     * @return Lista de salas.
     */
    List<Sala> listar();

    /**
     * Regras de negócio para atualizar uma sala.
     * @param sala A sala com os dados atualizados.
     * @throws SalaNaoEncontradaException Se a sala não for encontrada.
     * @throws CampoVazioException Se o 'nome' estiver vazio.
     * @throws Exception (para outras regras de negócio)
     */
    void atualizar(Sala sala) throws SalaNaoEncontradaException, CampoVazioException, Exception;

    /**
     * Regras de negócio para remover uma sala.
     * @param nome O nome da sala a ser removida.
     * @throws SalaNaoEncontradaException Se a sala não for encontrada.
     * @throws CampoVazioException Se 'nome' estiver vazio.
     */
    void remover(String nome) throws SalaNaoEncontradaException, CampoVazioException;
}
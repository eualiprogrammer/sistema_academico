package com.example.sea.business;

import com.example.sea.model.*; 
import com.example.sea.exceptions.*; 
import java.time.LocalDateTime;
import java.util.List;

public interface IControladorPalestra {

    /**
     * Regras de negócio para cadastrar uma nova palestra.
     * Este método recebe os dados, valida-os e cria o objeto Palestra.
     * @param titulo Título da palestra.
     * @param descricao Descrição da palestra.
     * @param evento O objeto Evento ao qual a palestra pertence.
     * @param dataHoraInicio A data e hora de início.
     * @param duracaoHoras A duração em horas.
     * @param sala O objeto Sala onde a palestra ocorrerá.
     * @param palestrante O objeto Palestrante que ministrará.
     * @throws PalestraJaExisteException Se o título já estiver em uso.
     * @throws CampoVazioException Se campos obrigatórios estiverem vazios.
     * @throws DataInvalidaException Se a data for inválida.
     * @throws ConflitoHorarioException Se a Sala ou o Palestrante já estiverem ocupados.
     */
    void cadastrar(String titulo, String descricao, Evento evento, 
                   LocalDateTime dataHoraInicio, float duracaoHoras, 
                   Sala sala, Palestrante palestrante) 
                   throws PalestraJaExisteException, CampoVazioException, DataInvalidaException, ConflitoHorarioException;

    /**
     * Busca uma palestra pelo seu título.
     * @param titulo O título da palestra.
     * @return A Palestra encontrada.
     * @throws PalestraNaoEncontradaException Se a palestra não for encontrada.
     * @throws CampoVazioException Se 'titulo' estiver vazio.
     */
    Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException;

    /**
     * Retorna todas as palestras cadastradas.
     * @return Lista de palestras.
     */
    List<Palestra> listar();
    
    /**
     * Retorna todas as palestras de um evento.
     * @param evento O evento.
     * @return Lista de palestras daquele evento.
     */
    List<Palestra> listarPorEvento(Evento evento);


    /**
     * Regras de negócio para atualizar uma palestra.
     * @param palestra O objeto palestra com os dados atualizados.
     * @throws PalestraNaoEncontradaException Se a palestra não for encontrada.
     * @throws CampoVazioException Se campos obrigatórios estiverem vazios.
     * @throws DataInvalidaException Se a data for inválida.
     * @throws ConflitoHorarioException Se a Sala ou o Palestrante já estiverem ocupados.
     */
    void atualizar(Palestra palestra) 
        throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException;

    /**
     * Regras de negócio para remover uma palestra.
     *
     * @param titulo O título da palestra a ser removida.
     * @return
     * @throws PalestraNaoEncontradaException Se a palestra não for encontrada.
     * @throws CampoVazioException            Se 'titulo' estiver vazio.
     */
    List<Palestra> remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException, PalestraComInscritosException;

    List<Palestra> listarTodos();
}
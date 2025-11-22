package com.example.sea.business;

import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException;
import java.util.List;


 // Define as regras de negócio e validações para a entidade Participante.
 
public interface IControladorParticipante {

    /**
     * Regras de negócio para cadastrar um novo participante.
     * @param participante O participante a ser cadastrado.
     * @throws ParticipanteJaExisteException Se o participante já existir.
     * @throws CampoVazioException Se campos obrigatórios (nome, email, cpf) estiverem vazios.
     */
    void cadastrar(Participante participante) throws ParticipanteJaExisteException, CampoVazioException;

    /**
     * Busca um participante pelo seu CPF.
     * @param cpf O CPF do participante.
     * @return O Participante encontrado.
     * @throws ParticipanteNaoEncontradoException Se o participante não for encontrado.
     * @throws CampoVazioException Se 'cpf' estiver vazio.
     */
    Participante buscar(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException;

    /**
     * Retorna todos os participantes cadastrados.
     * @return Lista de participantes.
     */
    List<Participante> listar();

    /**
     * Regras de negócio para atualizar um participante.
     * @param participante O participante com os dados atualizados.
     * @throws ParticipanteNaoEncontradoException Se o participante não for encontrado.
     * @throws CampoVazioException Se campos obrigatórios estiverem vazios.
     */
    void atualizar(Participante participante) throws ParticipanteNaoEncontradoException, CampoVazioException;

    /**
     * Regras de negócio para remover um participante.
     * @param cpf O CPF do participante a ser removido.
     * @throws ParticipanteNaoEncontradoException Se o participante não for encontrado.
     * @throws CampoVazioException Se 'cpf' estiver vazio.
     */
    void remover(String cpf) throws ParticipanteNaoEncontradoException, CampoVazioException;

    List<Participante> listarTodos();
}
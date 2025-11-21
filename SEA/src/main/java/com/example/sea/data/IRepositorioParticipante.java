package com.example.sea.data;

import com.example.sea.model.Participante;
import com.example.sea.exceptions.ParticipanteJaExisteException;
import com.example.sea.exceptions.ParticipanteNaoEncontradoException;
import java.util.List;

public interface IRepositorioParticipante {

    /**
     * Salva um novo participante no repositório.
     * @param participante O objeto Participante a ser salvo.
     * @throws ParticipanteJaExisteException Se um participante com o mesmo email ou CPF já existir.
     */
    void salvar(Participante participante) throws ParticipanteJaExisteException;

    /**
     * Busca um participante pelo seu identificador (vamos usar o CPF).
     * @param cpf O CPF do participante.
     * @return O objeto Participante encontrado.
     * @throws ParticipanteNaoEncontradoException Se o participante não for encontrado.
     */
    Participante buscarPorIdentificador(String cpf) throws ParticipanteNaoEncontradoException;

    /**
     * Retorna uma lista com todos os participantes cadastrados.
     * @return Uma List<Participante>.
     */
    List<Participante> listarTodos();

    /**
     * Atualiza os dados de um participante já existente (identificado pelo CPF).
     * @param participante O objeto Participante com os dados atualizados.
     * @throws ParticipanteNaoEncontradoException Se o participante a ser atualizado não for encontrado.
     */
    void atualizar(Participante participante) throws ParticipanteNaoEncontradoException;

    /**
     * Remove um participante do repositório.
     * @param cpf O CPF do participante a ser removido.
     * @throws ParticipanteNaoEncontradoException Se o participante a ser removido não for encontrado.
     */
    void deletar(String cpf) throws ParticipanteNaoEncontradoException;
}
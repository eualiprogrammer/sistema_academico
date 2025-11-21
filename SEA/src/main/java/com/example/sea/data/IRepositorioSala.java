package com.example.sea.data; // <-- Pacote 'data'

import com.example.sea.model.Sala;
import com.example.sea.exceptions.SalaJaExisteException;
import com.example.sea.exceptions.SalaNaoEncontradaException;
import java.util.List;

public interface IRepositorioSala {

    /**
     * Salva uma nova sala no repositório.
     * @param sala O objeto Sala a ser salvo.
     * @throws SalaJaExisteException Se uma sala com o mesmo nome já existir.
     */
    void salvar(Sala sala) throws SalaJaExisteException;

    /**
     * Busca uma sala pelo seu nome (que usaremos como identificador único).
     * @param nome O nome da sala (ex: "Auditório A").
     * @return O objeto Sala encontrado.
     * @throws SalaNaoEncontradaException Se a sala não for encontrada.
     */
    Sala buscarPorNome(String nome) throws SalaNaoEncontradaException;

    /**
     * Retorna uma lista com todas as salas cadastradas.
     * @return Uma List<Sala>.
     */
    List<Sala> listarTodas();

    /**
     * Atualiza os dados de uma sala já existente (identificada pelo nome).
     * @param sala O objeto Sala com os dados atualizados.
     * @throws SalaNaoEncontradaException Se a sala a ser atualizada não for encontrada.
     */
    void atualizar(Sala sala) throws SalaNaoEncontradaException;

    /**
     * Remove uma sala do repositório.
     * @param nome O nome da sala a ser removida.
     * @throws SalaNaoEncontradaException Se a sala a ser removida não for encontrada.
     */
    void deletar(String nome) throws SalaNaoEncontradaException;
}
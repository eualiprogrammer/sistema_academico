package com.example.sea.data;

import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import java.util.List;

public interface IRepositorioPalestrante {
    /**
     * Salva um novo palestrante.
     * @throws PalestranteJaExisteException Se o email já estiver em uso.
     */
    void salvar(Palestrante palestrante) throws PalestranteJaExisteException;
    /**
     * Busca um palestrante pelo seu identificador (email).
     * @return O objeto Palestrante.
     * @throws PalestranteNaoEncontradoException Se o palestrante não for encontrado.
     */
    Palestrante buscarPorIdentificador(String identificador) throws PalestranteNaoEncontradoException;
    /**
     * Lista todos os palestrantes.
     * @return Uma lista de todos os palestrantes.
     */
    List<Palestrante> listarTodos();

    /**
     * Atualiza um palestrante.
     * @throws PalestranteNaoEncontradoException Se o palestrante a ser atualizado não for encontrado.
     */
    void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException;

    /**
     * Remove um palestrante.
     * @throws PalestranteNaoEncontradoException Se o palestrante a ser removido não for encontrado.
     */
    void deletar(String identificador) throws PalestranteNaoEncontradoException;
}
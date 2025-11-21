package com.example.sea.business;

import com.example.sea.model.Palestrante;
import com.example.sea.exceptions.PalestranteJaExisteException;
import com.example.sea.exceptions.PalestranteNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException; // <-- ADICIONADO
import java.util.List;

public interface IControladorPalestrante {

    /**
     * @throws PalestranteJaExisteException Se o email já estiver em uso.
     * @throws CampoVazioException Se 'nome' ou 'email' estiverem vazios.
     */
    void cadastrar(Palestrante palestrante) throws PalestranteJaExisteException, CampoVazioException;

    /**
     * @throws PalestranteNaoEncontradoException Se o palestrante não for encontrado.
     * @throws CampoVazioException Se 'email' estiver vazio.
     */
    Palestrante buscar(String email) throws PalestranteNaoEncontradoException, CampoVazioException;

    List<Palestrante> listar();

    /**
     * @throws PalestranteNaoEncontradoException Se o palestrante não for encontrado.
     * @throws CampoVazioException Se 'email' estiver vazio.
     */
    void atualizar(Palestrante palestrante) throws PalestranteNaoEncontradoException, CampoVazioException;

    /**
     * @throws PalestranteNaoEncontradoException Se o palestrante não for encontrado.
     * @throws CampoVazioException Se 'email' estiver vazio.
     */
    void remover(String email) throws PalestranteNaoEncontradoException, CampoVazioException;
}
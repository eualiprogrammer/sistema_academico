package com.example.sea.data;

import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;
import java.util.List;

/**
 * Interface para o Repositório de Certificados.
 */
public interface IRepositorioCertificado {

    /**
     * Salva um novo certificado gerado.
     * @param certificado O objeto Certificado.
     */
    void salvar(Certificado certificado);

    /**
     * Busca um certificado pelo seu código único de validação.
     * @param codigoValidacao O código UUID.
     * @return O Certificado encontrado.
     * @throws CertificadoNaoEncontradoException Se não existir.
     */
    Certificado buscarPorCodigo(String codigoValidacao) throws CertificadoNaoEncontradoException;

    /**
     * Retorna todos os certificados do sistema.
     */
    List<Certificado> listarTodos();

    /**
     * Retorna todos os certificados de um participante específico (Histórico - REQ15).
     * @param participante O participante.
     * @return Lista de certificados dele.
     */
    List<Certificado> listarPorParticipante(Participante participante);

    /**
     * Remove um certificado (ex: se foi gerado por engano).
     * @param codigoValidacao O código do certificado.
     * @throws CertificadoNaoEncontradoException Se não existir.
     */
    void deletar(String codigoValidacao) throws CertificadoNaoEncontradoException;
}
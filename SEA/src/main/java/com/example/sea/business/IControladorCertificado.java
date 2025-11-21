package com.example.sea.business;

import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException;

import java.util.List;

/**
 * Interface (Contrato) para a camada de negócio (Serviço) de Certificado.
 * Foca-se na consulta e histórico (REQ15).
 */
public interface IControladorCertificado {

    /**
     * Busca um certificado pelo seu código de validação UUID.
     * @param codigoValidacao O código único.
     * @return O Certificado encontrado.
     * @throws CertificadoNaoEncontradoException Se não existir.
     * @throws CampoVazioException Se o código for vazio.
     */
    Certificado buscar(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException;

    /**
     * Retorna todos os certificados do sistema (para o admin).
     */
    List<Certificado> listarTodos();

    /**
     * Retorna o histórico de certificados de um participante (REQ15).
     * @param participante O participante a consultar.
     * @return Lista de certificados desse participante.
     * @throws IllegalArgumentException Se o participante for nulo.
     */
    List<Certificado> listarPorParticipante(Participante participante);

    /**
     * Remove um certificado (apenas para gestão, caso tenha erro).
     * @param codigoValidacao O código do certificado.
     * @throws CertificadoNaoEncontradoException Se não existir.
     * @throws CampoVazioException Se o código for vazio.
     */
    void remover(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException;
}
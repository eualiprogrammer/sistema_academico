package com.example.sea.business;
import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;
import com.example.sea.exceptions.CampoVazioException;

import java.util.List;

public interface IControladorCertificado {
    Certificado buscar(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException;

    List<Certificado> listarTodos();

    List<Certificado> listarPorParticipante(Participante participante);

    void remover(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException;
}
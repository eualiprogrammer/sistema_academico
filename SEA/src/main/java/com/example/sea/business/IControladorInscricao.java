package com.example.sea.business;

import com.example.sea.model.Certificado;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.*;
import java.util.List;

public interface IControladorInscricao {

    void inscrever(Participante participante, Palestra palestra) 
        throws InscricaoJaExisteException, LotacaoExcedidaException, ConflitoHorarioException, 
               PalestraNaoEncontradaException, ParticipanteNaoEncontradoException;

    void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException;

    void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException;

    List<Inscricao> listarPorParticipante(Participante participante);
    
    List<Inscricao> listarPorPalestra(Palestra palestra);

    Certificado gerarCertificado(Inscricao inscricao) 
        throws InscricaoNaoEncontradaException, CertificadoSemPresencaException;

    List<Inscricao> listarTodos();

    void atualizar(Inscricao inscricao) throws Exception;

    void cadastrar(Inscricao inscricao) throws Exception;
}

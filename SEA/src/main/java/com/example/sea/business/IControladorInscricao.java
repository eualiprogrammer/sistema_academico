package com.example.sea.business;

// Importa os modelos e exceções
import com.example.sea.model.Certificado;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.*; // Importa todas as nossas exceções
import java.util.List;

/**
 * Interface (Contrato) para a camada de negócio (Serviço) de Inscrição.
 * Define as regras de negócio mais complexas (Lotação, Conflito, Presença).
 */
public interface IControladorInscricao {

    /**
     * Regra de negócio principal: Inscrever um participante numa palestra.
     * @param participante O participante a ser inscrito.
     * @param palestra A palestra-alvo.
     * @throws InscricaoJaExisteException Se o participante já estiver inscrito.
     * @throws LotacaoExcedidaException Se a palestra estiver lotada (REQ23).
     * @throws ConflitoHorarioException Se o participante tiver conflito de horário (REQ24).
     * @throws PalestraNaoEncontradaException (Vem do repositório)
     * @throws ParticipanteNaoEncontradoException (Vem do repositório)
     */
    void inscrever(Participante participante, Palestra palestra) 
        throws InscricaoJaExisteException, LotacaoExcedidaException, ConflitoHorarioException, 
               PalestraNaoEncontradaException, ParticipanteNaoEncontradoException;

    /**
     * Cancela uma inscrição.
     * @param inscricao A inscrição a ser cancelada.
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada.
     */
    void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException;

    /**
     * Marca a presença de um participante numa inscrição (REQ09).
     * @param inscricao A inscrição onde a presença será marcada.
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada.
     */
    void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException;

    /**
     * Lista todas as inscrições de um participante específico.
     * (Usado na tela "Minhas Inscrições").
     * @param participante O participante a ser consultado.
     * @return Uma lista das suas inscrições.
     */
    List<Inscricao> listarPorParticipante(Participante participante);
    
    /**
     * Lista todas as inscrições de uma palestra específica.
     * (Usado na tela "Registrar Presença").
     * @param palestra A palestra a ser consultada.
     * @return Uma lista dos inscritos.
     */
    List<Inscricao> listarPorPalestra(Palestra palestra);

    /**
     * Gera um certificado para uma inscrição, validando a presença (REQ13, REQ26).
     * @param inscricao A inscrição a ser certificada.
     * @return O objeto Certificado criado.
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada.
     * @throws CertificadoSemPresencaException Se a presença não estiver marcada (REQ26).
     */
    Certificado gerarCertificado(Inscricao inscricao) 
        throws InscricaoNaoEncontradaException, CertificadoSemPresencaException;
}
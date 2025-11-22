package com.example.sea.data;

import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.InscricaoJaExisteException;
import com.example.sea.exceptions.InscricaoNaoEncontradaException;
import java.util.List;

/**
 * Interface (Contrato) para o Repositório de Inscrição.
 * Define as operações CRUD obrigatórias para a entidade Inscricao.
 */
public interface IRepositorioInscricao {

    /**
     * Salva uma nova inscrição no repositório.
     * @param inscricao O objeto Inscricao a ser salvo.
     * @throws InscricaoJaExisteException Se este participante já estiver inscrito nesta palestra.
     */
    void salvar(Inscricao inscricao) throws InscricaoJaExisteException;

    /**
     * Busca uma inscrição específica pela combinação de participante e palestra.
     * @param participante O participante da inscrição.
     * @param palestra A palestra da inscrição.
     * @return O objeto Inscricao encontrado.
     * @throws InscricaoNaoEncontradaException Se a inscrição não for encontrada.
     */
    Inscricao buscar(Participante participante, Palestra palestra) throws InscricaoNaoEncontradaException;

    /**
     * Retorna uma lista com TODAS as inscrições cadastradas.
     * @return Uma List<Inscricao>.
     */
    List<Inscricao> listarTodas();
    
    /**
     * [MUITO IMPORTANTE] Retorna todas as inscrições de uma palestra específica.
     * Este método é essencial para a regra de negócio de Lotação (REQ11).
     * @param palestra A palestra a ser consultada.
     * @return Uma List<Inscricao> daquela palestra.
     */
    List<Inscricao> listarPorPalestra(Palestra palestra);
    
    /**
     * [MUITO IMPORTANTE] Retorna todas as inscrições de um participante específico.
     * Este método é essencial para as telas "Minhas Inscrições" e "Meus Certificados".
     * @param participante O participante a ser consultado.
     * @return Uma List<Inscricao> daquele participante.
     */
    List<Inscricao> listarPorParticipante(Participante participante);

    /**
     * Atualiza os dados de uma inscrição (ex: marcar presença).
     * @param inscricao O objeto Inscricao com os dados atualizados.
     * @throws InscricaoNaoEncontradaException Se a inscrição a ser atualizada não for encontrada.
     */
    void atualizar(Inscricao inscricao) throws InscricaoNaoEncontradaException;

    /**
     * Remove uma inscrição do repositório (cancela a inscrição).
     * @param inscricao A inscrição a ser removida.
     * @throws InscricaoNaoEncontradaException Se a inscrição a ser removida não for encontrada.
     */
    void deletar(Inscricao inscricao) throws InscricaoNaoEncontradaException;

}
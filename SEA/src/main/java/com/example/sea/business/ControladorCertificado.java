package com.example.sea.business;

import com.example.sea.data.IRepositorioCertificado;
import com.example.sea.data.RepositorioCertificado; 
import com.example.sea.model.Certificado;
import com.example.sea.model.Participante;
import com.example.sea.exceptions.CampoVazioException;
import com.example.sea.exceptions.CertificadoNaoEncontradoException;

import java.util.List;

/**
 * IMPLEMENTAÇÃO da camada de negócio (Serviço) de Certificado.
 * Foca-se na consulta (histórico) e remoção.
 */
public class ControladorCertificado implements IControladorCertificado {

    // --- Ligação com a Camada de Dados ---
    private IRepositorioCertificado repositorioCertificado;

    /**
     * Construtor: Instancia o repositório.
     */
    public ControladorCertificado() {
        this.repositorioCertificado = new RepositorioCertificado();
    }

    // --- MÉTODOS DE NEGÓCIO (Implementação da Interface) ---

    @Override
    public Certificado buscar(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException {
        // --- REGRA DE NEGÓCIO: Validação de Campos ---
        if (codigoValidacao == null || codigoValidacao.trim().isEmpty()) {
            throw new CampoVazioException("Código de Validação");
        }

        // Repassa a chamada para a camada 'data'
        return this.repositorioCertificado.buscarPorCodigo(codigoValidacao);
    }

    @Override
    public List<Certificado> listarTodos() {
        // Apenas repassa a chamada
        return this.repositorioCertificado.listarTodos();
    }

    @Override
    public List<Certificado> listarPorParticipante(Participante participante) {
        // Validação básica
        if (participante == null) {
            throw new IllegalArgumentException("O participante não pode ser nulo para buscar o histórico.");
        }

        // Repassa a chamada
        return this.repositorioCertificado.listarPorParticipante(participante);
    }

    @Override
    public void remover(String codigoValidacao) throws CertificadoNaoEncontradoException, CampoVazioException {
        // Validação
        if (codigoValidacao == null || codigoValidacao.trim().isEmpty()) {
            throw new CampoVazioException("Código de Validação");
        }

        // Repassa a chamada
        this.repositorioCertificado.deletar(codigoValidacao);
    }
}
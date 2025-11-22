package com.example.sea.business;

import com.example.sea.data.*;
import com.example.sea.model.*;
import com.example.sea.exceptions.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class ControladorInscricao implements IControladorInscricao {

    private IRepositorioInscricao repositorioInscricao;

    // --- CORREÇÃO 1: Declarar a variável aqui ---
    private IRepositorioCertificado repositorioCertificado;

    public ControladorInscricao() {
        this.repositorioInscricao = new RepositorioInscricao();

        // --- CORREÇÃO 2: Inicializar a variável aqui ---
        this.repositorioCertificado = new RepositorioCertificado();
    }

    private void validarConflitoHorario(Participante participante, Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        LocalDateTime fimNova = inicioNova.plusHours((long) novaPalestra.getDuracaoHoras());

        List<Inscricao> inscricoesExistentes = this.repositorioInscricao.listarPorParticipante(participante);

        for (Inscricao inscricaoExistente : inscricoesExistentes) {
            Palestra palestraExistente = inscricaoExistente.getPalestra();
            LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
            LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

            if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                throw new ConflitoHorarioException(palestraExistente.getTitulo(), novaPalestra.getTitulo());
            }
        }
    }

    @Override
    public void inscrever(Participante participante, Palestra palestra)
            throws InscricaoJaExisteException, LotacaoExcedidaException, ConflitoHorarioException,
            PalestraNaoEncontradaException, ParticipanteNaoEncontradoException {

        if (participante == null) throw new ParticipanteNaoEncontradoException("null");
        if (palestra == null) throw new PalestraNaoEncontradaException("null");

        int numeroInscritos = this.repositorioInscricao.listarPorPalestra(palestra).size();
        int capacidadeSala = palestra.getSala().getCapacidade();

        if (numeroInscritos >= capacidadeSala) {
            throw new LotacaoExcedidaException(palestra.getTitulo());
        }

        this.validarConflitoHorario(participante, palestra);

        Inscricao novaInscricao = new Inscricao(participante, palestra);
        this.repositorioInscricao.salvar(novaInscricao);
    }

    @Override
    public void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        this.repositorioInscricao.deletar(inscricao);
    }

    @Override
    public void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        // 1. Marca presença no objeto
        inscricao.confirmarPresenca();

        // 2. Atualiza a inscrição no banco de dados
        this.repositorioInscricao.atualizar(inscricao);

        // 3. GERA O CERTIFICADO AUTOMATICAMENTE E SALVA
        try {
            Certificado novoCertificado = new Certificado(inscricao);

            // --- AQUI ESTAVA O ERRO ANTES (Agora vai funcionar) ---
            this.repositorioCertificado.salvar(novoCertificado);

            System.out.println("Presença confirmada e Certificado gerado: " + novoCertificado.getCodigoValidacao());

        } catch (Exception e) {
            System.err.println("Erro ao gerar certificado automático: " + e.getMessage());
        }
    }

    @Override
    public List<Inscricao> listarPorParticipante(Participante participante) {
        if (participante == null) return new ArrayList<>();
        return this.repositorioInscricao.listarPorParticipante(participante);
    }

    @Override
    public List<Inscricao> listarPorPalestra(Palestra palestra) {
        if (palestra == null) return new ArrayList<>();
        return this.repositorioInscricao.listarPorPalestra(palestra);
    }

    @Override
    public Certificado gerarCertificado(Inscricao inscricao)
            throws InscricaoNaoEncontradaException, CertificadoSemPresencaException {

        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        if (!inscricao.isPresenca()) {
            throw new CertificadoSemPresencaException(
                inscricao.getParticipante().getNome(),
                inscricao.getPalestra().getTitulo()
            );
        }

        Certificado novoCertificado = new Certificado(inscricao);

        try {
            this.repositorioCertificado.salvar(novoCertificado);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return novoCertificado;
    }

    @Override
    public List<Inscricao> listarTodos() {
        return this.repositorioInscricao.listarTodas();
    }

    @Override
    public void atualizar(Inscricao inscricao) throws Exception {
        if (inscricao == null) throw new IllegalArgumentException("Inscrição inválida");
        this.repositorioInscricao.atualizar(inscricao);
    }

    @Override
    public void cadastrar(Inscricao inscricao) throws Exception {
        this.repositorioInscricao.salvar(inscricao);
    }
}
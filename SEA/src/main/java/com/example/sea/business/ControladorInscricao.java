package com.example.sea.business;

import com.example.sea.data.*;
import com.example.sea.model.*;
import com.example.sea.exceptions.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

public class ControladorInscricao implements IControladorInscricao {

    private IRepositorioInscricao repositorioInscricao;
    private IRepositorioCertificado repositorioCertificado;

    public ControladorInscricao() {
        this.repositorioInscricao = new RepositorioInscricao();
        this.repositorioCertificado = new RepositorioCertificado();
    }

    private void validarConflitoHorario(Participante participante, Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        long minutosDuracao = (long) (novaPalestra.getDuracaoHoras() * 60);
        LocalDateTime fimNova = inicioNova.plusMinutes(minutosDuracao);

        List<Inscricao> inscricoesExistentes = this.repositorioInscricao.listarPorParticipante(participante);

        for (Inscricao inscricao : inscricoesExistentes) {
            if (inscricao.getAtividade() instanceof Palestra) {
                Palestra palestraExistente = (Palestra) inscricao.getAtividade();

                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                long minutosExistente = (long) (palestraExistente.getDuracaoHoras() * 60);
                LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);

                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException(palestraExistente.getTitulo(), novaPalestra.getTitulo());
                }
            }
        }
    }

    @Override
    public void inscrever(Participante participante, Atividade atividade) throws Exception {
        // 1. Validações Básicas
        if (participante == null) throw new ParticipanteNaoEncontradoException("Participante nulo");
        if (atividade == null) throw new IllegalArgumentException("Atividade nula");

        // --- LÓGICA PARA PALESTRA (Individual) ---
        if (atividade instanceof Palestra) {
            Palestra palestra = (Palestra) atividade;

            // A. Validação de Lotação
            int numeroInscritos = this.repositorioInscricao.listarPorPalestra(palestra).size();
            int capacidadeSala = palestra.getSala().getCapacidade();

            if (numeroInscritos >= capacidadeSala) {
                throw new LotacaoExcedidaException(palestra.getTitulo());
            }

            // B. Validação de Conflito de Horário
            this.validarConflitoHorario(participante, palestra);
        }

        // --- LÓGICA PARA WORKSHOP (Pacote) ---
        // Se for um Workshop, inscrevemos automaticamente em todas as palestras dele!
        if (atividade instanceof Workshop) {
            Workshop workshop = (Workshop) atividade;

            if (workshop.getPalestrasDoWorkshop().isEmpty()) {
                // Opcional: Avisar que está vazio? Por enquanto deixamos passar.
            }

            for (Palestra palestraDoPacote : workshop.getPalestrasDoWorkshop()) {
                try {
                    // Chamada Recursiva: Inscreve na palestra individualmente
                    // Isso garante que conflitos e lotação sejam checados para cada uma!
                    this.inscrever(participante, palestraDoPacote);
                } catch (InscricaoJaExisteException e) {
                    // Se já está inscrito numa das palestras, ignoramos e continuamos para as outras
                    System.out.println("Já inscrito na palestra: " + palestraDoPacote.getTitulo());
                }
            }
        }

        // 4. Salvar a Inscrição Principal (seja no Workshop ou na Palestra)
        // Isso garante que o item apareça em "Minhas Inscrições"
        Inscricao novaInscricao = new Inscricao(participante, atividade);
        this.repositorioInscricao.salvar(novaInscricao);
    }

    @Override
    public void inscrever(Participante participante, Palestra palestra) throws Exception {
        this.inscrever(participante, (Atividade) palestra);
    }

    @Override
    public void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        this.repositorioInscricao.deletar(inscricao);
    }

    @Override
    public void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        inscricao.confirmarPresenca();

        this.repositorioInscricao.atualizar(inscricao);

        try {
            Certificado novoCertificado = new Certificado(inscricao);

            this.repositorioCertificado.salvar(novoCertificado);

            inscricao.setCertificado(novoCertificado);
            this.repositorioInscricao.atualizar(inscricao);

            System.out.println("Presença confirmada e Certificado gerado: " + novoCertificado.getCodigoValidacao());

        } catch (Exception e) {
            System.err.println("Erro ao gerar certificado automático: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Certificado gerarCertificado(Inscricao inscricao)
            throws InscricaoNaoEncontradaException, CertificadoSemPresencaException {

        if (inscricao == null) throw new InscricaoNaoEncontradaException();

        if (!inscricao.isPresenca()) {
            throw new CertificadoSemPresencaException(
                    inscricao.getParticipante().getNome(),
                    inscricao.getAtividade().getTitulo()
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

    @Override
    public List<Inscricao> listarPorAtividade(Atividade atividade) {
        return this.repositorioInscricao.listarPorAtividade(atividade);
    }
}
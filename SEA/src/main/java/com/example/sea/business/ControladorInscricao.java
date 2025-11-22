package com.example.sea.business;

import com.example.sea.data.*; 
import com.example.sea.model.*; 
import com.example.sea.exceptions.*; 
import java.util.ArrayList; 
import java.time.LocalDateTime;
import java.util.List;

public class ControladorInscricao implements IControladorInscricao {

    private IRepositorioInscricao repositorioInscricao;

    public ControladorInscricao() {
        this.repositorioInscricao = new RepositorioInscricao();
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
                throw new ConflitoHorarioException(
                    palestraExistente.getTitulo(), 
                    novaPalestra.getTitulo()
                );
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
        
        inscricao.confirmarPresenca();
        
        this.repositorioInscricao.atualizar(inscricao);
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

        this.repositorioInscricao.atualizar(inscricao);
        
        System.out.println("Certificado gerado: " + novoCertificado.getCodigoValidacao());
        return novoCertificado;
    }

    @Override
    public List<Inscricao> listarTodos() {
        return this.repositorioInscricao.listarTodas();
    }

    @Override
    public void atualizar(Inscricao inscricao) throws Exception {
        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição inválida");
        }
        this.repositorioInscricao.atualizar(inscricao);
    }

    @Override
    public void cadastrar(Inscricao inscricao) throws Exception {
        this.repositorioInscricao.salvar(inscricao);
    }
}

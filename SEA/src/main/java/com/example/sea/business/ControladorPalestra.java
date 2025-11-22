package com.example.sea.business;

import java.util.ArrayList; 
import com.example.sea.data.*; 
import com.example.sea.model.*; 
import com.example.sea.exceptions.*; 
import java.time.LocalDateTime;
import java.util.List;

public class ControladorPalestra implements IControladorPalestra {
    private IRepositorioPalestra repositorioPalestra;
    private IRepositorioInscricao repositorioInscricao;

    public ControladorPalestra() {
        this.repositorioPalestra = new RepositorioPalestra();
        this.repositorioInscricao = new RepositorioInscricao();
    }

    private void validarConflitos(Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        LocalDateTime fimNova = inicioNova.plusHours((long) novaPalestra.getDuracaoHoras());

        for (Palestra palestraExistente : this.repositorioPalestra.listarTodas()) {
            if (palestraExistente.getSala().getNome().equals(novaPalestra.getSala().getNome())) {
                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException("A Sala '" + novaPalestra.getSala().getNome() +
                            "' já está em uso neste horário pela palestra: " + palestraExistente.getTitulo());
                }
            }
        }

        for (Palestra palestraExistente : this.repositorioPalestra.listarTodas()) {
            if (palestraExistente.getPalestrante().getEmail().equals(novaPalestra.getPalestrante().getEmail())) {
                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException("O palestrante '" + novaPalestra.getPalestrante().getNome() +
                            "' já está ocupado neste horário com a palestra: " + palestraExistente.getTitulo());
                }
            }
        }
    }

    @Override
    public void cadastrar(String titulo, String descricao, Evento evento,
                          LocalDateTime dataHoraInicio, float duracaoHoras,
                          Sala sala, Palestrante palestrante)
            throws PalestraJaExisteException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        if (evento == null) throw new IllegalArgumentException("Evento não pode ser nulo");
        if (dataHoraInicio == null) throw new CampoVazioException("Data/Hora de Início");
        if (sala == null) throw new IllegalArgumentException("Sala não pode ser nula");
        if (palestrante == null) throw new IllegalArgumentException("Palestrante não pode ser nulo");

        if (duracaoHoras <= 0) throw new DataInvalidaException("Duração", "A duração deve ser maior que zero.");

        Palestra novaPalestra = new Palestra(titulo, descricao, evento, dataHoraInicio, duracaoHoras, sala, palestrante);

        this.validarConflitos(novaPalestra);

        this.repositorioPalestra.salvar(novaPalestra);
    }

    @Override
    public Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new CampoVazioException("Título");
        }
        return this.repositorioPalestra.buscarPorTitulo(titulo);
    }

    @Override
    public List<Palestra> listar() {
        return this.repositorioPalestra.listarTodas();
    }

    @Override
    public List<Palestra> listarPorEvento(Evento evento) {
        if (evento == null) return new ArrayList<>();
        return this.repositorioPalestra.listarPorEvento(evento);
    }

    @Override
    public void atualizar(Palestra palestra)
            throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (palestra == null) throw new IllegalArgumentException("Palestra não pode ser nula.");
        if (palestra.getTitulo() == null || palestra.getTitulo().trim().isEmpty())
            throw new CampoVazioException("Título");
        if (palestra.getDuracaoHoras() <= 0)
            throw new DataInvalidaException("Duração", "A duração deve ser maior que zero.");

        this.repositorioPalestra.atualizar(palestra);
    }

    @Override
    public List<Palestra> remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException, PalestraComInscritosException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new CampoVazioException("Título");
        }

        Palestra palestraParaRemover = this.repositorioPalestra.buscarPorTitulo(titulo);
        List<Inscricao> inscritos = this.repositorioInscricao.listarPorPalestra(palestraParaRemover);

        if (!inscritos.isEmpty()) {
            throw new PalestraComInscritosException(titulo, inscritos.size());
        }

        this.repositorioPalestra.deletar(titulo);
        System.out.println("Palestra removida: " + titulo);

        return this.repositorioPalestra.listarTodas();
    }

    @Override
    public List<Palestra> listarTodos() {
        return repositorioPalestra.listarTodas();
    }
}

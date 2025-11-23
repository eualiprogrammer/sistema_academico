package com.example.sea.business;

import com.example.sea.data.*;
import com.example.sea.exceptions.*;
import com.example.sea.model.Evento;
import com.example.sea.model.Inscricao;
import com.example.sea.model.Palestra;
import com.example.sea.model.Palestrante;
import com.example.sea.model.Sala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ControladorPalestra implements IControladorPalestra {

    private IRepositorioPalestra repositorioPalestra;
    private IRepositorioInscricao repositorioInscricao;
    private IRepositorioEvento repositorioEvento;

    public ControladorPalestra() {
        this.repositorioPalestra = new RepositorioPalestra();
        this.repositorioInscricao = new RepositorioInscricao();
        this.repositorioEvento = new RepositorioEvento();
    }

    // --- VALIDAÇÃO 1: Data deve estar dentro do período do Evento ---
    private void validarDataDentroDoEvento(LocalDateTime dataPalestra, Evento evento) throws DataInvalidaException {
        if (evento == null || evento.getDataInicio() == null || evento.getDataFim() == null) {
            return; // Sem datas no evento, deixa passar
        }

        LocalDateTime inicioEvento = evento.getDataInicio().atStartOfDay();
        LocalDateTime fimEvento = evento.getDataFim().atTime(23, 59, 59);

        if (dataPalestra.isBefore(inicioEvento) || dataPalestra.isAfter(fimEvento)) {
            throw new DataInvalidaException(
                    "Data da Palestra",
                    "A palestra deve ocorrer entre " + evento.getDataInicio() + " e " + evento.getDataFim()
            );
        }
    }

    // --- VALIDAÇÃO 2: Conflito de Sala e Palestrante ---
    private void validarConflitos(Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        long minutosNova = (long) (novaPalestra.getDuracaoHoras() * 60);
        LocalDateTime fimNova = inicioNova.plusMinutes(minutosNova);

        for (Palestra existente : this.repositorioPalestra.listarTodas()) {
            // Pula a própria palestra na edição (se tiverem o mesmo ID/Título)
            if (existente.getTitulo().equals(novaPalestra.getTitulo())) continue;

            LocalDateTime inicioExistente = existente.getDataHoraInicio();
            long minutosExistente = (long) (existente.getDuracaoHoras() * 60);
            LocalDateTime fimExistente = inicioExistente.plusMinutes(minutosExistente);

            // Verifica Interseção de Horário
            boolean conflitoHorario = inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente);

            if (conflitoHorario) {
                // Mesma Sala?
                if (existente.getSala().getNome().equals(novaPalestra.getSala().getNome())) {
                    throw new ConflitoHorarioException(
                            "A Sala " + novaPalestra.getSala().getNome() + " já está ocupada por: " + existente.getTitulo()
                    );
                }
                // Mesmo Palestrante?
                if (existente.getPalestrante().getEmail().equals(novaPalestra.getPalestrante().getEmail())) {
                    throw new ConflitoHorarioException(
                            "O palestrante " + novaPalestra.getPalestrante().getNome() + " já tem compromisso na palestra: " + existente.getTitulo()
                    );
                }
            }
        }
    }

    @Override
    public void cadastrar(String titulo, String descricao, Evento evento,
                          LocalDateTime dataHoraInicio, float duracaoHoras,
                          Sala sala, Palestrante palestrante)
            throws PalestraJaExisteException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        // 1. Validações Básicas
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        if (evento == null) throw new IllegalArgumentException("Selecione um Evento");
        if (sala == null) throw new IllegalArgumentException("Selecione uma Sala");
        if (palestrante == null) throw new IllegalArgumentException("Selecione um Palestrante");
        if (dataHoraInicio == null) throw new CampoVazioException("Data e Hora");
        if (duracaoHoras <= 0) throw new DataInvalidaException("Duração", "A duração deve ser positiva.");

        // 2. Valida Regras de Negócio
        validarDataDentroDoEvento(dataHoraInicio, evento);

        // Cria objeto temporário para validar conflitos
        Palestra novaPalestra = new Palestra(titulo, descricao, evento, dataHoraInicio, duracaoHoras, sala, palestrante);
        evento.adicionarAtividade(novaPalestra);

        try {
            this.repositorioEvento.atualizar(evento);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Salva
        this.repositorioPalestra.salvar(novaPalestra);
    }

    @Override
    public void atualizar(Palestra palestra)
            throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        if (palestra == null) throw new IllegalArgumentException("Palestra nula");

        // Revalida as regras na edição
        validarDataDentroDoEvento(palestra.getDataHoraInicio(), palestra.getEvento());
        validarConflitos(palestra);

        this.repositorioPalestra.atualizar(palestra);
    }

    @Override
    public void remover(String titulo) throws PalestraNaoEncontradaException, CampoVazioException, PalestraComInscritosException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");

        Palestra palestra = this.repositorioPalestra.buscarPorTitulo(titulo);

        // Regra: Não pode apagar se tiver inscritos
        List<Inscricao> inscritos = this.repositorioInscricao.listarPorPalestra(palestra);
        if (!inscritos.isEmpty()) {
            throw new PalestraComInscritosException(titulo, inscritos.size());
        }

        this.repositorioPalestra.deletar(titulo);
    }

    @Override
    public Palestra buscar(String titulo) throws PalestraNaoEncontradaException, CampoVazioException {
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        return this.repositorioPalestra.buscarPorTitulo(titulo);
    }

    @Override
    public List<Palestra> listarTodos() {
        return this.repositorioPalestra.listarTodas();
    }

    // Mantém compatibilidade se a interface pedir listar()
    public List<Palestra> listar() {
        return this.listarTodos();
    }

    @Override
    public List<Palestra> listarPorEvento(Evento evento) {
        if (evento == null) return new ArrayList<>();
        return this.repositorioPalestra.listarPorEvento(evento);
    }
}
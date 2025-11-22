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

    /**
     * Construtor: Quando o Controlador é criado, ele instancia
     * TODOS os repositórios de que precisa.
     */
    public ControladorPalestra() {
        this.repositorioPalestra = new RepositorioPalestra();
        this.repositorioInscricao = new RepositorioInscricao();
    }

    /**
     * Valida se uma nova palestra (data/hora/duração) entra em conflito
     * com palestras existentes para a mesma Sala ou mesmo Palestrante.
     *
     * @param novaPalestra A palestra a ser validada.
     * @throws ConflitoHorarioException Se houver conflito.
     */
    private void validarConflitos(Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        LocalDateTime fimNova = inicioNova.plusHours((long) novaPalestra.getDuracaoHoras()); // Simplificado

        // 1. Verifica todas as palestras que USAM A MESMA SALA
        for (Palestra palestraExistente : this.repositorioPalestra.listarTodas()) {
            if (palestraExistente.getSala().getNome().equals(novaPalestra.getSala().getNome())) {
                // Se for a mesma sala, verifica o horário
                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

                // Lógica de sobreposição de tempo
                if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                    throw new ConflitoHorarioException("A Sala '" + novaPalestra.getSala().getNome() +
                            "' já está em uso neste horário pela palestra: " + palestraExistente.getTitulo());
                }
            }
        }

        // 2. Verifica todas as palestras DADAS PELO MESMO PALESTRANTE (REQ24)
        for (Palestra palestraExistente : this.repositorioPalestra.listarTodas()) {
            if (palestraExistente.getPalestrante().getEmail().equals(novaPalestra.getPalestrante().getEmail())) {
                // Se for o mesmo palestrante, verifica o horário
                LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
                LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

                // Lógica de sobreposição de tempo
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

        // --- REGRA 1: Validação de Campos Vazios ---
        if (titulo == null || titulo.trim().isEmpty()) throw new CampoVazioException("Título");
        if (evento == null) throw new IllegalArgumentException("Evento não pode ser nulo");
        if (dataHoraInicio == null) throw new CampoVazioException("Data/Hora de Início");
        if (sala == null) throw new IllegalArgumentException("Sala não pode ser nula");
        if (palestrante == null) throw new IllegalArgumentException("Palestrante não pode ser nulo");

        // --- REGRA 2: Validação de Valores ---
        if (duracaoHoras <= 0) throw new DataInvalidaException("Duração", "A duração deve ser maior que zero.");

        // --- REGRA 3: Cria o objeto Palestra ---
        Palestra novaPalestra = new Palestra(titulo, descricao, evento, dataHoraInicio, duracaoHoras, sala, palestrante);

        // --- REGRA 4: Validação de Conflitos (REQ24) ---
        this.validarConflitos(novaPalestra); // Verifica conflito de sala e palestrante

        // --- REGRA 5: Salvar (Se tudo passou) ---
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
        if (evento == null) return new ArrayList<>(); // Retorna lista vazia se o evento for nulo
        return this.repositorioPalestra.listarPorEvento(evento);
    }

    @Override
    public void atualizar(Palestra palestra)
            throws PalestraNaoEncontradaException, CampoVazioException, DataInvalidaException, ConflitoHorarioException {

        // Validações
        if (palestra == null) throw new IllegalArgumentException("Palestra não pode ser nula.");
        if (palestra.getTitulo() == null || palestra.getTitulo().trim().isEmpty())
            throw new CampoVazioException("Título");
        if (palestra.getDuracaoHoras() <= 0)
            throw new DataInvalidaException("Duração", "A duração deve ser maior que zero.");

        // (Nota: A validação de conflito para 'atualizar' é mais complexa,
        // pois temos que ignorar a própria palestra que está a ser atualizada.
        // Vamos manter a validação simples por agora.)

        // this.validarConflitos(palestra); // (Precisaria de lógica extra para ignorar a si mesma)

        // Chama a camada 'data'
        this.repositorioPalestra.atualizar(palestra);
    }

    // ... código anterior ...

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

        // Retorna a lista atualizada
        return this.repositorioPalestra.listarTodas();
    } // <--- FECHA O MÉTODO REMOVER AQUI

    // O método listarTodos deve ficar FORA, como um método da classe
    @Override
    public List<Palestra> listarTodos() {
        return repositorioPalestra.listarTodas();
    }
} // <--- FECHA A CLASSE AQUI
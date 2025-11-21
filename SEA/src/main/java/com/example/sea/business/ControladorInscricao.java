package com.example.sea.business;


import com.example.sea.data.*; 
import com.example.sea.model.*; 
import com.example.sea.exceptions.*; 
import java.util.ArrayList; 
import java.time.LocalDateTime;
import java.util.List;

/**
 * IMPLEMENTAÇÃO da camada de negócio (Serviço) de Inscrição.
 * Contém as regras de negócio de Lotação (REQ11), Conflito (REQ24) e Presença (REQ09/26).
 */
public class ControladorInscricao implements IControladorInscricao {

    // --- Ligação com TODAS as Camadas de Dados necessárias ---
    private IRepositorioInscricao repositorioInscricao;
    
    // (O RepositorioSala não é preciso aqui, pois a Palestra já contém a Sala)

    /**
     * Construtor: Instancia todos os repositórios de que este controlador precisa.
     */
    public ControladorInscricao() {
        this.repositorioInscricao = new RepositorioInscricao();
    }

    // --- Método Privado de Validação de Conflito (REQ24) ---
    /**
     * Valida se um participante já está inscrito noutra palestra no mesmo horário.
     * @param participante O participante a verificar.
     * @param novaPalestra A palestra em que ele se quer inscrever.
     * @throws ConflitoHorarioException Se houver conflito.
     */
    private void validarConflitoHorario(Participante participante, Palestra novaPalestra) throws ConflitoHorarioException {
        LocalDateTime inicioNova = novaPalestra.getDataHoraInicio();
        LocalDateTime fimNova = inicioNova.plusHours((long) novaPalestra.getDuracaoHoras());

        // 1. Busca TODAS as inscrições que este participante já tem
        List<Inscricao> inscricoesExistentes = this.repositorioInscricao.listarPorParticipante(participante);

        // 2. Compara os horários de cada uma
        for (Inscricao inscricaoExistente : inscricoesExistentes) {
            Palestra palestraExistente = inscricaoExistente.getPalestra();
            
            LocalDateTime inicioExistente = palestraExistente.getDataHoraInicio();
            LocalDateTime fimExistente = inicioExistente.plusHours((long) palestraExistente.getDuracaoHoras());

            // Lógica de sobreposição de tempo
            if (inicioNova.isBefore(fimExistente) && fimNova.isAfter(inicioExistente)) {
                // Se houver sobreposição, lança a exceção
                throw new ConflitoHorarioException(
                    palestraExistente.getTitulo(), 
                    novaPalestra.getTitulo()
                );
            }
        }
    }

    // --- MÉTODOS DE NEGÓCIO (Implementação da Interface) ---

    @Override
    public void inscrever(Participante participante, Palestra palestra) 
        throws InscricaoJaExisteException, LotacaoExcedidaException, ConflitoHorarioException, 
               PalestraNaoEncontradaException, ParticipanteNaoEncontradoException {
        
        // --- REGRA 1: Validação de Nulidade ---
        if (participante == null) throw new ParticipanteNaoEncontradoException("null");
        if (palestra == null) throw new PalestraNaoEncontradaException("null");

        // --- REGRA 2: Validação de Lotação (REQ11 e REQ23) ---
        // 2a. Conta quantos já estão inscritos
        int numeroInscritos = this.repositorioInscricao.listarPorPalestra(palestra).size();
        // 2b. Busca a capacidade da sala
        int capacidadeSala = palestra.getSala().getCapacidade();
        
        if (numeroInscritos >= capacidadeSala) {
            // Se estiver cheio, lança a exceção
            throw new LotacaoExcedidaException(palestra.getTitulo());
        }

        // --- REGRA 3: Validação de Conflito de Horário (REQ24) ---
        this.validarConflitoHorario(participante, palestra);

        // --- REGRA 4: Salvar ---
        // Se passou em todas as regras, cria o objeto 'Inscricao'
        // (O construtor da Inscrição já faz a ligação mútua)
        Inscricao novaInscricao = new Inscricao(participante, palestra);
        
        // Chama a camada 'data' para salvar
        // (O repositório 'salvar' já trata a InscricaoJaExisteException)
        this.repositorioInscricao.salvar(novaInscricao);
    }

    @Override
    public void cancelarInscricao(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        // (Regra de Negócio Faltante - REQ25: Não permitir cancelar se já tiver presença?)
        // Por agora, vamos permitir
        this.repositorioInscricao.deletar(inscricao);
    }

    @Override
    public void marcarPresenca(Inscricao inscricao) throws InscricaoNaoEncontradaException {
        if (inscricao == null) throw new InscricaoNaoEncontradaException();
        
        // 1. Modifica o objeto em memória
        inscricao.confirmarPresenca();
        
        // 2. Salva a modificação no ficheiro .dat
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

        // --- REGRA DE NEGÓCIO: Validação de Presença (REQ26) ---
        if (!inscricao.isPresenca()) {
            throw new CertificadoSemPresencaException(
                inscricao.getParticipante().getNome(), 
                inscricao.getPalestra().getTitulo()
            );
        }

        // 1. Cria o objeto Certificado
        // (O construtor do Certificado já valida a presença e faz a ligação mútua)
        Certificado novoCertificado = new Certificado(inscricao);

        // 2. ATUALIZA a Inscrição no repositório
        // (Para salvar a ligação com o certificado que acabou de ser criado)
        this.repositorioInscricao.atualizar(inscricao);
        
        System.out.println("Certificado gerado: " + novoCertificado.getCodigoValidacao());
        return novoCertificado;
    }
}
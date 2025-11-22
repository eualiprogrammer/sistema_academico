package com.example.sea;

import com.example.sea.business.SistemaSGA;
import com.example.sea.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MainTeste {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TESTE DO SISTEMA SGA ===");
        
        // 1. Obter a instância do sistema (Fachada)
        SistemaSGA sistema = SistemaSGA.getInstance();

        try {
            // --- 1. CADASTRAR SALAS ---
            System.out.println("\n>> 1. Cadastrando Salas...");
            Sala salaA = new Sala("Auditório A", 100, 1, 2);
            Sala salaB = new Sala("Laboratório 1", 30, 1, 0);
            
            sistema.getControladorSala().cadastrar(salaA);
            sistema.getControladorSala().cadastrar(salaB);
            System.out.println("Salas cadastradas com sucesso!");

            // --- 2. CADASTRAR PALESTRANTES ---
            System.out.println("\n>> 2. Cadastrando Palestrantes...");
            Palestrante palestrante1 = new Palestrante("Dr. Silva", "IA", "81999999999", "silva@email.com");
            Palestrante palestrante2 = new Palestrante("Dra. Ana", "Java", "81988888888", "ana@email.com");

            sistema.getControladorPalestrante().cadastrar(palestrante1);
            sistema.getControladorPalestrante().cadastrar(palestrante2);
            System.out.println("Palestrantes cadastrados com sucesso!");

            // --- 3. CADASTRAR EVENTO ---
            System.out.println("\n>> 3. Cadastrando Evento...");
            Evento evento = new Evento("Semana da Tecnologia", LocalDate.now(), LocalDate.now().plusDays(5), "Evento Anual");
            sistema.getControladorEvento().cadastrar(evento);
            System.out.println("Evento cadastrado: " + evento.getNome());

            // --- 4. CADASTRAR PALESTRAS ---
            System.out.println("\n>> 4. Cadastrando Palestras...");
            LocalDateTime dataPalestra1 = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            LocalDateTime dataPalestra2 = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0);

            // Palestra 1: IA no Auditório A com Dr. Silva
            sistema.getControladorPalestra().cadastrar(
                "Introdução a IA", "Conceitos básicos", evento, 
                dataPalestra1, 2.0f, salaA, palestrante1
            );

            // Palestra 2: Java Avançado no Lab 1 com Dra. Ana
            sistema.getControladorPalestra().cadastrar(
                "Java Avançado", "Streams e Lambdas", evento, 
                dataPalestra2, 3.0f, salaB, palestrante2
            );
            System.out.println("Palestras cadastradas com sucesso!");

            // --- 5. CADASTRAR PARTICIPANTE ---
            System.out.println("\n>> 5. Cadastrando Participante...");
            Participante aluno = new Participante("João Aluno", "joao@email.com", "UFPE", "123.456.789-00", "leu69");
            sistema.getControladorParticipante().cadastrar(aluno);
            System.out.println("Participante cadastrado: " + aluno.getNome());

            // --- 6. REALIZAR INSCRIÇÃO ---
            System.out.println("\n>> 6. Inscrevendo aluno na palestra de IA...");
            // Primeiro buscamos os objetos
            Participante p = sistema.getControladorParticipante().buscar("123.456.789-00");
            Palestra palestraIA = sistema.getControladorPalestra().buscar("Introdução a IA");

            sistema.getControladorInscricao().inscrever(p, palestraIA);
            System.out.println("Inscrição realizada com sucesso!");

            // --- 7. TENTAR GERAR CERTIFICADO (SEM PRESENÇA) ---
            System.out.println("\n>> 7. Tentando gerar certificado sem presença (Deve falhar)...");
            // A busca agora depende da sua implementação do buscarInscricao no controlador. 
            // Vamos listar por participante para encontrar a inscrição
            List<Inscricao> inscricoesJoao = sistema.getControladorInscricao().listarPorParticipante(p);
            Inscricao inscricaoDoJoao = inscricoesJoao.get(0);

            try {
                sistema.getControladorInscricao().gerarCertificado(inscricaoDoJoao);
            } catch (Exception e) {
                System.out.println("SUCESSO NO TESTE DE FALHA: " + e.getMessage());
            }

            // --- 8. MARCAR PRESENÇA ---
            System.out.println("\n>> 8. Marcando presença...");
            sistema.getControladorInscricao().marcarPresenca(inscricaoDoJoao);
            System.out.println("Presença confirmada!");

            // --- 9. GERAR CERTIFICADO (COM SUCESSO) ---
            System.out.println("\n>> 9. Gerando certificado agora...");
            Certificado cert = sistema.getControladorInscricao().gerarCertificado(inscricaoDoJoao);
            System.out.println("Certificado gerado! Código: " + cert.getCodigoValidacao());
            System.out.println("Carga Horária: " + cert.getCargaHorariaRegistrada() + " horas.");

            System.out.println("\n=== TESTE CONCLUÍDO COM SUCESSO! ===");

        } catch (Exception e) {
            System.err.println("ERRO DURANTE O TESTE:");
            e.printStackTrace();
        }
    }
}
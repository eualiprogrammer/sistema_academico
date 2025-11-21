package com.example.sea.business;

/**
 * Esta classe inicializa e guarda todos os Controladores do sistema.
 * A GUI (telas) vai falar APENAS com esta classe.
 */
public class SistemaSGA {

    // 1. A instância única (Singleton)
    private static SistemaSGA instance;

    // 2. Referências para todos os 8 Controladores (Gerentes)
    private IControladorPalestrante controladorPalestrante;
    private IControladorSala controladorSala;
    private IControladorParticipante controladorParticipante;
    private IControladorEvento controladorEvento;
    private IControladorPalestra controladorPalestra;
    private IControladorWorkshop controladorWorkshop;
    private IControladorInscricao controladorInscricao;
    private IControladorCertificado controladorCertificado;

    /**
     * Construtor privado: Inicializa todos os controladores de uma só vez.
     * Ninguém fora desta classe pode fazer 'new SistemaSGA()'.
     */
    private SistemaSGA() {
        System.out.println("Inicializando o Sistema SGA...");
        
        this.controladorPalestrante = new ControladorPalestrante();
        this.controladorSala = new ControladorSala();
        this.controladorParticipante = new ControladorParticipante();
        this.controladorEvento = new ControladorEvento();
        this.controladorPalestra = new ControladorPalestra();
        this.controladorWorkshop = new ControladorWorkshop();
        this.controladorInscricao = new ControladorInscricao();
        this.controladorCertificado = new ControladorCertificado();
    }

    /**
     * Método estático para obter a instância única do Sistema.
     * Se ainda não existir, cria. Se já existir, devolve a mesma.
     * @return A instância de SistemaSGA.
     */
    public static SistemaSGA getInstance() {
        if (instance == null) {
            instance = new SistemaSGA();
        }
        return instance;
    }

    // --- Getters para a GUI acessar as funcionalidades ---

    public IControladorPalestrante getControladorPalestrante() {
        return controladorPalestrante;
    }

    public IControladorSala getControladorSala() {
        return controladorSala;
    }

    public IControladorParticipante getControladorParticipante() {
        return controladorParticipante;
    }

    public IControladorEvento getControladorEvento() {
        return controladorEvento;
    }

    public IControladorPalestra getControladorPalestra() {
        return controladorPalestra;
    }

    public IControladorWorkshop getControladorWorkshop() {
        return controladorWorkshop;
    }

    public IControladorInscricao getControladorInscricao() {
        return controladorInscricao;
    }

    public IControladorCertificado getControladorCertificado() {
        return controladorCertificado;
    }
}
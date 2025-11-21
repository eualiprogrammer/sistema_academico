package com.example.sea.model;
import java.util.ArrayList;
import java.util.List;

public class Workshop extends Atividade{
    private static final long serialVersionUID = 9L;
    private List<Palestra> palestrasDoWorkshop;

    public Workshop(String titulo, String descricao, Evento evento) {
        super(titulo, descricao, evento);
        
        this.palestrasDoWorkshop = new ArrayList<>();
    }

    public List<Palestra> getPalestrasDoWorkshop() {
        return this.palestrasDoWorkshop;
    }

    public void adicionarPalestra(Palestra palestra) {
        if (palestra != null && !this.palestrasDoWorkshop.contains(palestra)) {
            this.palestrasDoWorkshop.add(palestra);
        }
    }

    public void removerPalestra(Palestra palestra) {
        this.palestrasDoWorkshop.remove(palestra);
    }
}
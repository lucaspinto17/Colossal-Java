package com.example;

public class Feitico {
    private String nome;
    private String tipo; // "Projetil", "Area", "Canalizado"
    private String escola; // "Elemental", "Arcano", "Sombrio"
    private int custoMana;
    private double tempoRecarga;
    private double poderBase;

    public Feitico(String nome, String tipo, String escola, 
        int custoMana, double tempoRecarga, double poderBase) {
        this.nome = nome;
        this.tipo = tipo;
        this.escola = escola;
        this.custoMana = custoMana;
        this.tempoRecarga = tempoRecarga;
        this.poderBase = poderBase;
    }

    public String getNomeFeitico() {
        return nome;
    }

    public void setNomeFeitico(String nome) {
        this.nome = nome;
    }

    public String getTipoFeitico() {
        return tipo;
    }

    public void setTipoFeitico(String tipo) {
        this.tipo = tipo;
    }

    public String getEscolaFeitico() {
        return escola;
    }

    public void setEscolaFeitico(String escola) {
        this.escola = escola;
    }

    public int getCustoManaFeitico() {
        return custoMana;
    }

    public void setCustoManaFeitico(int custoMana) {
        this.custoMana = custoMana;
    }

    public double getTempoRecargaFeitico() {
        return tempoRecarga;
    }

    public void setTempoRecargaFeitico(double tempoRecarga) {
        this.tempoRecarga = tempoRecarga;
    }

    public double getPoderBaseFeitico() {
        return poderBase;
    }

    public void setPoderBaseFeitico(double poderBase) {
        this.poderBase = poderBase;
    }
    
}

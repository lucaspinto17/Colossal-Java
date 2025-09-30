

import java.util.ArrayList;

public class Feitico {
    private String nome;
    private String tipo;
    private String escola;
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
    
    public void aplicarDano(Personagem lancador, Personagem alvo) {
        int dano = calcularDano(lancador, alvo);
        
        switch (this.tipo) {
            case "Projetil":
                alvo.receberDano(dano);
                System.out.println(alvo.getCodinome() + " recebeu " + dano + " de dano de projétil!");
                break;
                
            case "Canalizado":
                int danoCanalizado = dano * 2;
                alvo.receberDano(danoCanalizado);
                System.out.println(alvo.getCodinome() + " recebeu " + danoCanalizado + " de dano canalizado!");
                break;
                
            case "Area":
                System.out.println("Use aplicarDanoArea para feitiços de área");
                break;
        }
    }
    
    public void aplicarDanoArea(Personagem lancador, ArrayList<Personagem> alvos) {
        if (!this.tipo.equals("Area")) {
            System.out.println("Este feitiço não é de área!");
            return;
        }
        
        System.out.println("Feitiço de área atingiu " + alvos.size() + " alvos!");
        
        for (Personagem alvo : alvos) {
            int dano = calcularDano(lancador, alvo);
            int danoArea = (int)(dano * 0.7);
            alvo.receberDano(danoArea);
            System.out.println(alvo.getCodinome() + " recebeu " + danoArea + " de dano de área!");
        }
    }
    
    private int calcularDano(Personagem lancador, Personagem alvo) {
        int danoBase = (int)this.poderBase + lancador.getPoderBase();
        int danoFinal = danoBase - alvo.getResistenciaMagica();
        return Math.max(danoFinal, 1);
    }
}
package com.example;
import java.util.ArrayList;


public class Mago extends Personagem {
    private String escola; 
    private String foco;   
   
    private int manaAtual;


    private ArrayList <Feitico> grimorio = new ArrayList<>();//
    private int quantidadeFeiticos;
    
    
    public Mago(String id, String codinome, int manaAtual, int vida, int manaMax, int poderBase, int resistenciaMagica, String escola, String foco, int quantidadeFeiticos) {
        
        super(id, codinome, vida, manaMax, poderBase, resistenciaMagica);
        this.escola = escola;
        this.foco = foco;
        grimorio = new ArrayList<>();
        this.quantidadeFeiticos = quantidadeFeiticos;
    }

    private String definirFocoPorEscola(String escola) {
        if (escola.equals("Elemental")) {
            return "Vara";
        } else if (escola.equals("Arcano")) {
            return "Cajado";
        } else if (escola.equals("Sombrio")) {
            return "Tomo";
        }
        return "Vara"; // padrão
    }

    
    public int getManaAtual(){
        return manaAtual;
    }
    public void gastarMana(int valor){
        this.manaAtual -= valor;
    }
    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
        this.foco = definirFocoPorEscola(escola);
    }

     public String getFoco() {
        return foco;
    }


    public void aprenderFeitico(Mago id, Feitico f){
    
       grimorio.add(f);        
       System.out.println("O " + getId() + " " + "Aprendeu"+ " "+ f.getNomeFeitico()  );

    }
    public void listarFeitico(){
        
            for (Feitico feitico : grimorio) {
            
            System.out.println("Feitiços existentes: " + feitico.getNomeFeitico());
        }
    }

    public void utilizarFeitico(Feitico f) {
    if (f.getCustoManaFeitico() > this.getManaAtual()) {
        System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
    } else {
        this.gastarMana(f.getCustoManaFeitico());
        System.out.println("O mago lançou o feitiço " + f.getNomeFeitico());
    }
}
}

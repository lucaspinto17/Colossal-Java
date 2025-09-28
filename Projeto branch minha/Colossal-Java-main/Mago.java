

import java.util.ArrayList;

public class Mago extends Personagem {
    private String escola; 
    private String foco;   
    private int manaAtual;
    private ArrayList<Feitico> grimorio = new ArrayList<>();
    private int quantidadeFeiticos;
    
    public Mago(String id, String codinome, int manaAtual, int vida, int manaMax, 
                int poderBase, int resistenciaMagica, String escola, String foco, 
                int quantidadeFeiticos) {
        super(id, codinome, vida, manaMax, poderBase, resistenciaMagica);
        this.manaAtual = manaAtual;
        this.escola = escola;
        this.foco = foco;
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
        return "Vara";
    }
    
    public int getManaAtual() {
        return manaAtual;
    }
    
    public void gastarMana(int valor) {
        this.manaAtual -= valor;
        if (this.manaAtual < 0) {
            this.manaAtual = 0;
        }
    }
    
    public void recuperarMana(int valor) {
        this.manaAtual += valor;
        if (this.manaAtual > this.getManaMax()) {
            this.manaAtual = this.getManaMax();
        }
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

    public void aprenderFeitico(Feitico f) {
        grimorio.add(f);        
        System.out.println("O " + getId() + " aprendeu " + f.getNomeFeitico());
    }
    
    public void listarFeitico() {
        if (grimorio.isEmpty()) {
            System.out.println("O grimório está vazio!");
            return;
        }
        
        for (Feitico feitico : grimorio) {
            System.out.println("Feitiço: " + feitico.getNomeFeitico() + " - Tipo: " + feitico.getTipoFeitico());
        }
    }

    public void utilizarFeitico(Feitico f, Personagem alvo) {
        if (f.getCustoManaFeitico() > this.getManaAtual()) {
            System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
        } else {
            this.gastarMana(f.getCustoManaFeitico());
            System.out.println("O mago " + this.getCodinome() + " lançou o feitiço " + f.getNomeFeitico());
            f.aplicarDano(this, alvo);
        }
    }
    
    public void utilizarFeiticoArea(Feitico f, ArrayList<Personagem> alvos) {
        if (f.getCustoManaFeitico() > this.getManaAtual()) {
            System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
        } else {
            this.gastarMana(f.getCustoManaFeitico());
            System.out.println("O mago " + this.getCodinome() + " lançou o feitiço " + f.getNomeFeitico());
            f.aplicarDanoArea(this, alvos);
        }
    }
    
    public void utilizarFeiticoCanalizado(Feitico f, Personagem alvo, int turnoPreparacao) {
        if (f.getCustoManaFeitico() > this.getManaAtual()) {
            System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
        } else if (turnoPreparacao > 0) {
            this.gastarMana(f.getCustoManaFeitico() / 2);
            System.out.println("Preparando " + f.getNomeFeitico() + " - " + turnoPreparacao + " turno(s) restante(s)");
        } else {
            this.gastarMana(f.getCustoManaFeitico() / 2);
            System.out.println("O mago " + this.getCodinome() + " lançou o feitiço canalizado " + f.getNomeFeitico());
            f.aplicarDano(this, alvo);
        }
    }
}
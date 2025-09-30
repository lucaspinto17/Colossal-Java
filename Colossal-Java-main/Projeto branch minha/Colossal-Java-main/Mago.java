import java.util.ArrayList;

public class Mago extends Personagem {
    private String escola; 
    private String foco;   
    private int manaAtual;
    private ArrayList<Feitico> grimorio = new ArrayList<>();
    private int quantidadeFeiticos;
    
    // Sistema de Estados
    private boolean silenciado;
    private boolean atordoado;
    private boolean manaBurn;
    private int turnosRestantesSilencio;
    private int turnosRestantesAtordoamento;
    private int turnosRestantesManaBurn;
    
    // Variáveis fornecidas
    public static final double bonusAreaElemental = 1.3;
    public static double penalidadeElementalCanalizado = 0.7;
    public static double chanceCriticoArcano = 0.5;
    public static double multiplicadorCriticoArcano = 1.5;
    public static double quantidadeDrenoSombrio = 0.1;
    
    public Mago(String id, String codinome, int manaAtual, int vida, int manaMax, 
                int poderBase, int resistenciaMagica, String escola, String foco, 
                int quantidadeFeiticos) {
        super(id, codinome, vida, manaMax, poderBase, resistenciaMagica);
        this.manaAtual = manaAtual;
        this.escola = escola;
        this.foco = foco;
        this.quantidadeFeiticos = quantidadeFeiticos;
        
        // Inicializar estados
        this.silenciado = false;
        this.atordoado = false;
        this.manaBurn = false;
        this.turnosRestantesSilencio = 0;
        this.turnosRestantesAtordoamento = 0;
        this.turnosRestantesManaBurn = 0;
    }

    public String definirFocoPorEscola(String escola) {
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
        
        System.out.println(this.getCodinome()+"Escola alterada para: " + escola + " - Foco: " + this.foco);
        switch (escola) {
            case "Elemental":
                System.out.println("Bônus: Feitiços de Área +30%, Penalidade: Feitiços Canalizados -30%");
                break;
            case "Arcano":
                System.out.println("Bônus: 50% de chance de crítico em Feitiços de Projétil");
                break;
            case "Sombrio":
                System.out.println("Bônus: Dreno de mana e escudos sombrios disponíveis");
                break;
        }
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

    // Métodos de Estado
    public void aplicarSilencio(int turnos) {
        this.silenciado = true;
        this.turnosRestantesSilencio = turnos;
        System.out.println(this.getCodinome() + " foi silenciado por " + turnos + " turnos!");
    }
    
    public void aplicarAtordoamento(int turnos) {
        this.atordoado = true;
        this.turnosRestantesAtordoamento = turnos;
        System.out.println(this.getCodinome() + " foi atordoado por " + turnos + " turnos!");
    }
    
    public void aplicarManaBurn(int turnos) {
        this.manaBurn = true;
        this.turnosRestantesManaBurn = turnos;
        System.out.println(this.getCodinome() + " sofreu Mana Burn por " + turnos + " turnos!");
    }
    
    public boolean estaSilenciado() {
        return silenciado;
    }
    
    public boolean estaAtordoado() {
        return atordoado;
    }
    
    public boolean temManaBurn() {
        return manaBurn;
    }

    // Métodos de Utilização de Feitiços
    public void utilizarFeitico(Feitico f, Personagem alvo) {
        if (!podeUtilizarFeitico()) {
            return;
        }
        
        if (f.getCustoManaFeitico() > this.getManaAtual()) {
            System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
        } else {
            int custoManaFinal = f.getCustoManaFeitico();
            
            if (this.escola.equals("Sombrio")) {
                aplicarEfeitosSombrios(f, alvo);
            }
            
            this.gastarMana(custoManaFinal);
            System.out.println("O mago " + this.getCodinome() + " lançou o feitiço " + f.getNomeFeitico());
            
            int danoBase = f.calcularDano(this, alvo);
            int danoComAfinidade = calcularDanoComAfinidade(f, danoBase);
            
            aplicarDanoComAfinidade(f, alvo, danoComAfinidade);
        }
    }
    
    public void utilizarFeiticoArea(Feitico f, ArrayList<Personagem> alvos) {
        if (!podeUtilizarFeitico()) {
            return;
        }
        
        if (f.getCustoManaFeitico() > this.getManaAtual()) {
            System.out.println("Mana insuficiente para lançar " + f.getNomeFeitico());
        } else {
            this.gastarMana(f.getCustoManaFeitico());
            System.out.println("O mago " + this.getCodinome() + " lançou o feitiço " + f.getNomeFeitico());
            f.aplicarDanoArea(this, alvos);
        }
    }
    
    public void utilizarFeiticoCanalizado(Feitico f, Personagem alvo, int turnoPreparacao) {
        if (!podeUtilizarFeitico()) {
            return;
        }
        
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

    // Métodos de Apoio
    public boolean podeUtilizarFeitico() {
        if (estaAtordoado()) {
            System.out.println(this.getCodinome() + " está atordoado e não pode agir!");
            return false;
        }
        
        if (estaSilenciado()) {
            System.out.println(this.getCodinome() + " está silenciado e não pode lançar feitiços!");
            return false;
        }
        
        return true;
    }
    
    public int calcularDanoComAfinidade(Feitico feitico, int danoBase) {
        double multiplicador = 1.0;
        
        switch (this.escola) {
            case "Elemental":
                if (feitico.getTipoFeitico().equals("Area")) {
                    multiplicador = bonusAreaElemental;
                    System.out.println("Bônus Elemental aplicado em feitiço de Área!");
                } else if (feitico.getTipoFeitico().equals("Canalizado")) {
                    multiplicador = penalidadeElementalCanalizado;
                    System.out.println("Penalidade Elemental aplicada em feitiço Canalizado!");
                }
                break;
                
            case "Arcano":
                if (feitico.getTipoFeitico().equals("Projetil")) {
                    if (Math.random() < chanceCriticoArcano) {
                        multiplicador = multiplicadorCriticoArcano;
                        System.out.println("Crítico Arcano! Dano aumentado!");
                    }
                }
                break;
                
            case "Sombrio":
                System.out.println("Poder sombrio emana do feitiço...");
                break;
        }
        
        return (int)(danoBase * multiplicador);
    }
    
    public void aplicarEfeitosSombrios(Feitico feitico, Personagem alvo) {
        int manaDrenada = (int)(feitico.getCustoManaFeitico() * quantidadeDrenoSombrio);
        this.recuperarMana(manaDrenada);
        System.out.println("Dreno sombrio: " + this.getCodinome() + " recuperou " + manaDrenada + " de mana");
        
        if (alvo instanceof Mago && Math.random() < 0.2) {
            ((Mago) alvo).aplicarManaBurn(2);
        }
    }
    
    public void aplicarDanoComAfinidade(Feitico feitico, Personagem alvo, int dano) {
        switch (feitico.getTipoFeitico()) {
            case "Projetil":
                alvo.receberDano(dano);
                System.out.println(alvo.getCodinome() + " recebeu " + dano + " de dano de projétil!");
                break;
                
            case "Canalizado":
                int danoCanalizado = dano * 2;
                alvo.receberDano(danoCanalizado);
                System.out.println(alvo.getCodinome() + " recebeu " + danoCanalizado + " de dano canalizado!");
                break;
        }
    }
    
    public void processarFimTurno() {
        if (silenciado) {
            turnosRestantesSilencio--;
            if (turnosRestantesSilencio <= 0) {
                silenciado = false;
                System.out.println(this.getCodinome() + " não está mais silenciado!");
            }
        }
        
        if (atordoado) {
            turnosRestantesAtordoamento--;
            if (turnosRestantesAtordoamento <= 0) {
                atordoado = false;
                System.out.println(this.getCodinome() + " não está mais atordoado!");
            }
        }
        
        if (manaBurn) {
            int drenoMana = (int)(this.getManaMax() * 0.05);
            this.manaAtual = Math.max(0, this.manaAtual - drenoMana);
            System.out.println(this.getCodinome() + " perdeu " + drenoMana + " de mana por Mana Burn!");
            
            turnosRestantesManaBurn--;
            if (turnosRestantesManaBurn <= 0) {
                manaBurn = false;
                System.out.println(this.getCodinome() + " não está mais sob efeito de Mana Burn!");
            }
        }
    }
    
    public void ativarEscudoSombrio() {
        if (!this.escola.equals("Sombrio")) {
            System.out.println("Apenas magos da escola Sombria podem ativar escudos sombrios!");
            return;
        }
        
        int custoMana = 30;
        if (this.manaAtual >= custoMana) {
            this.gastarMana(custoMana);
            System.out.println("Escudo Sombrio ativado! O mago está protegido por sombras...");
        } else {
            System.out.println("Mana insuficiente para ativar Escudo Sombrio!");
        }
    }
}
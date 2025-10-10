import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Mago {
    // --- Constantes ---
    public static final String VARA = "Vara";
    public static final String CAJADO = "Cajado";
    public static final String TOMO = "Tomo";
    public static final String HUMANO = "Humano";
    public static final String IA = "IA";
    public static final String IA_AGRESSIVO = "Agressivo";
    public static final String IA_OPORTUNISTA = "Oportunista";

    protected int id;
    protected String codinome, escola, foco, controlador, perfilIA;
    protected int manaMax, manaAtual, vidaMax, vidaAtual;
    protected double poderBase, resistenciaMagica;
    protected LocalDateTime horaEntrada;
    protected List<Feitico> grimorio = new ArrayList<>();
    private List<EfeitoStatus> efeitosAtivos = new ArrayList<>();

    public Mago(int id, String codinome, int manaMax, double poderBase, double resistencia, String foco, String controlador, String perfilIA) {
        this.id = id;
        this.codinome = codinome;
        this.manaMax = manaMax;
        this.manaAtual = manaMax;
        this.poderBase = poderBase;
        this.resistenciaMagica = resistencia;
        this.foco = foco;
        this.controlador = controlador;
        this.perfilIA = perfilIA;
        this.horaEntrada = LocalDateTime.now();
        this.vidaMax = 100;
        this.vidaAtual = this.vidaMax;
        inicializarGrimorio();
    }

    protected abstract void inicializarGrimorio();

    /**
     * Retorna a lista de feitiços que o mago pode lançar com a mana atual.
     * @return Uma lista de Feiticos disponíveis.
     */
    public List<Feitico> getFeiticosDisponiveis() {
        List<Feitico> possiveis = new ArrayList<>();
        for (Feitico f : this.grimorio) {
            if (this.manaAtual >= f.getCustoMana()) {
                possiveis.add(f);
            }
        }
        return possiveis;
    }

    // LÓGICA DE ESCOLHA DA IA (PERMANECE A MESMA)
    public Feitico escolherFeitico(List<Mago> inimigos) {
        if (possuiStatus(EfeitoStatus.SILENCIO)) {
            System.out.printf("%s está silenciado!%n", this.codinome);
            return null;
        }
        
        List<Feitico> possiveis = getFeiticosDisponiveis();
        if (possiveis.isEmpty()) return null;

        if (IA_AGRESSIVO.equals(this.perfilIA)) {
            Feitico maisPoderoso = possiveis.get(0);
            for (Feitico f : possiveis) {
                if (f.getPoderBase() > maisPoderoso.getPoderBase()) {
                    maisPoderoso = f;
                }
            }
            return maisPoderoso;
        } else {
            return possiveis.get((int) (Math.random() * possiveis.size()));
        }
    }
    
    public void receberDano(double dano) {
        this.vidaAtual -= dano;
        if (this.vidaAtual < 0) this.vidaAtual = 0;
    }

    public void adicionarEfeito(EfeitoStatus novoEfeito) {
        for (EfeitoStatus e : this.efeitosAtivos) {
            if (e.getTipo().equals(novoEfeito.getTipo())) return;
        }
        this.efeitosAtivos.add(novoEfeito);
        System.out.printf("   > %s foi afetado por %s!%n", this.codinome, novoEfeito.getTipo());
    }

    public void processarEfeitosDoTurno() {
        Iterator<EfeitoStatus> iterator = this.efeitosAtivos.iterator();
        while (iterator.hasNext()) {
            EfeitoStatus efeito = iterator.next();
            if (EfeitoStatus.MANA_BURN.equals(efeito.getTipo())) {
                this.manaAtual = Math.max(0, this.manaAtual - 10);
                System.out.printf("%s queima 10 de mana por um efeito! (Mana: %d)%n", this.codinome, this.manaAtual);
            }
            if (efeito.decrementarDuracao()) {
                iterator.remove();
                System.out.printf("O efeito '%s' em %s acabou.%n", efeito.getTipo(), this.codinome);
            }
        }
    }

    public boolean possuiStatus(String tipo) {
        for(EfeitoStatus e : this.efeitosAtivos) {
            if(e.getTipo().equals(tipo)) return true;
        }
        return false;
    }

    public void restaurarParaDuelo() {
        this.manaAtual = this.manaMax;
        this.vidaAtual = this.vidaMax;
        this.efeitosAtivos.clear();
    }

    // --- Getters ---
    public boolean estaVivo() { return this.vidaAtual > 0; }
    public int getId() { return this.id; }
    public String getCodinome() { return codinome; }
    public String getEscola() { return this.escola; }
    public String getControlador() { return this.controlador; }
    public double getPoderBase() { return poderBase; }
    public double getResistenciaMagica() { return resistenciaMagica; }
    public int getVidaAtual() { return vidaAtual; }
    public int getVidaMax() { return vidaMax; }
    public int getManaAtual() { return manaAtual; }
    public int getManaMax() { return manaMax; }
    public void consumirMana(int custo) { this.manaAtual -= custo; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | %s (%s) | Foco: %s", id, codinome, escola, foco);
    }
}
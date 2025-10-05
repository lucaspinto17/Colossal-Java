public abstract class Mago {
    protected int id;
    protected String codinome;
    protected String escola;
    protected int manaMax;
    protected int manaAtual;
    protected double poderBase;
    protected double resistenciaMagica;
    
    public Mago(int id, String codinome, int manaMax, double poderBase, double resistenciaMagica) {
        this.id = id;
        this.codinome = codinome;
        this.manaMax = manaMax;
        this.manaAtual = manaMax;
        this.poderBase = poderBase;
        this.resistenciaMagica = resistenciaMagica;
    }
    
    // Métodos abstratos para polimorfismo
    public abstract double calcularDano(String tipoFeitico);
    public abstract double calcularCustoMana(String tipoFeitico);
    
    // Getters
    public int getId() { return id; }
    public String getCodinome() { return codinome; }
    public String getEscola() { return escola; }
    public int getManaMax() { return manaMax; }
    public int getManaAtual() { return manaAtual; }
    public double getPoderBase() { return poderBase; }
    public double getResistenciaMagica() { return resistenciaMagica; }
    
    public boolean consumirMana(int quantidade) {
        if (manaAtual >= quantidade) {
            manaAtual -= quantidade;
            return true;
        }
        return false;
    }
    
    public void restaurarMana() {
        manaAtual = manaMax;
    }
    
    @Override
    public String toString() {
        return String.format("Mago [ID: %d, Codinome: %s, Escola: %s, Mana: %d/%d]", 
                           id, codinome, escola, manaAtual, manaMax);
    }
}
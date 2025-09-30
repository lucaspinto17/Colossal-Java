public abstract class Personagem {
    private String id;
    private String codinome;
    private int vida;
    private int manaMax;
    private int poderBase;
    private int resistenciaMagica;

    public Personagem(String id, String codinome, int vida, int manaMax, int poderBase, int resistenciaMagica) {
        this.id = id;
        this.codinome = codinome;
        this.vida = vida;
        this.manaMax = manaMax;
        this.poderBase = poderBase;
        this.resistenciaMagica = resistenciaMagica;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCodinome() {
        return codinome;
    }
    
    public void setCodinome(String codinome) {
        this.codinome = codinome;
    }
    
    public int getVida() {
        return vida;
    }
    
    public void setVida(int vida) {
        this.vida = vida;
    }
    
    public int getManaMax() {
        return manaMax;
    }
    
    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }
    
    public int getPoderBase() {
        return poderBase;
    }
    
    public void setPoderBase(int poderBase) {
        this.poderBase = poderBase;
    }
    
    public int getResistenciaMagica() {
        return resistenciaMagica;
    }
    
    public void setResistenciaMagica(int resistenciaMagica) {
        this.resistenciaMagica = resistenciaMagica;
    }

    public int calcularDano(Personagem alvo) {
        int dano = this.poderBase - alvo.getResistenciaMagica();
        return Math.max(dano, 0);
    }

    public void receberDano(int dano) {
        this.vida = this.vida - dano;
        if (this.vida < 0) {
            this.vida = 0;
        }
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }
}
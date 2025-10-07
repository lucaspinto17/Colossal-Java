/**
 * Define tudo sobre um feitiço: nome, custo, poder, e possíveis efeitos.
 */
public class Feitico {
    // --- Constantes de Tipos de Feitiço ---
    public static final String PROJETIL = "Projétil";
    public static final String AREA = "Área";

    private String nome;
    private String tipo;
    private int custoMana;
    private double poderBase;
    private String efeitoAplicado;
    private int duracaoEfeito;
    private double chanceDeAplicarEfeito;

    public Feitico(String nome, String tipo, int custoMana, double poderBase, String efeitoAplicado, int duracaoEfeito, double chanceDeAplicarEfeito) {
        this.nome = nome;
        this.tipo = tipo;
        this.custoMana = custoMana;
        this.poderBase = poderBase;
        this.efeitoAplicado = efeitoAplicado;
        this.duracaoEfeito = duracaoEfeito;
        this.chanceDeAplicarEfeito = chanceDeAplicarEfeito;
    }

    public Feitico(String nome, String tipo, int custoMana, double poderBase) {
        this(nome, tipo, custoMana, poderBase, null, 0, 0.0);
    }

    // --- Getters ---
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public int getCustoMana() { return custoMana; }
    public double getPoderBase() { return poderBase; }
    public String getEfeitoAplicado() { return efeitoAplicado; }
    public int getDuracaoEfeito() { return duracaoEfeito; }
    public double getChanceDeAplicarEfeito() { return chanceDeAplicarEfeito; }
}
/*
================================== FUNCIONALIDADES DA CLASSE ==================================
- Define os nomes dos tipos de feitiços (PROJETIL, AREA) como constantes.
- Modela um feitiço individual, guardando todas as suas propriedades, como nome, custo de mana, poder,
  e se ele pode aplicar um efeito de status (com duração e chance).
- Possui dois construtores: um completo para feitiços com efeitos e um simplificado para
  feitiços que apenas causam dano.
=============================================================================================
*/
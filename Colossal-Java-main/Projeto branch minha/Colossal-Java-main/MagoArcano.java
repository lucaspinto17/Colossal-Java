public class MagoArcano extends Mago {
    
    public MagoArcano(int id, String codinome, int manaMax, double poderBase, double resistenciaMagica) {
        super(id, codinome, manaMax, poderBase, resistenciaMagica);
        this.escola = "Arcano";
    }
    
    @Override
    public double calcularDano(String tipoFeitico) {
        double dano = poderBase;
        
        // Chance de crítico para projéteis
        if (tipoFeitico.equals("PROJETIL")) {
            if (Math.random() < 0.25) { // 25% de chance de crítico
                dano *= 1.5; // +50% de dano
                System.out.println("CRÍTICO! ");
            }
        }
        
        return dano;
    }
    
    @Override
    public double calcularCustoMana(String tipoFeitico) {
        return 20; // Custo fixo para Arcanos
    }
}
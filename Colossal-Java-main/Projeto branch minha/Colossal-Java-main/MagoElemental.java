public class MagoElemental extends Mago {
    
    public MagoElemental(int id, String codinome, int manaMax, double poderBase, double resistenciaMagica) {
        super(id, codinome, manaMax, poderBase, resistenciaMagica);
        this.escola = "Elemental";
    }
    
    @Override
    public double calcularDano(String tipoFeitico) {
        double dano = poderBase;
        
        // Bônus para feitiços de área, penalidade para canalizados
        switch (tipoFeitico) {
            case "AREA":
                dano *= 1.3; // +30% de dano
                break;
            case "CANALIZADO":
                dano *= 0.8; // -20% de dano
                break;
            case "PROJETIL":
                dano *= 1.0; // dano normal
                break;
        }
        
        return dano;
    }
    
    @Override
    public double calcularCustoMana(String tipoFeitico) {
        double custoBase = 20; // custo base de mana
        
        switch (tipoFeitico) {
            case "AREA":
                return custoBase * 0.9; // -10% de mana
            case "CANALIZADO":
                return custoBase * 1.1; // +10% de mana
            default:
                return custoBase;
        }
    }
}
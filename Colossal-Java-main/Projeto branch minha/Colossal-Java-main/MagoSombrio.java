public class MagoSombrio extends Mago {
    
    public MagoSombrio(int id, String codinome, int manaMax, double poderBase, double resistenciaMagica) {
        super(id, codinome, manaMax, poderBase, resistenciaMagica);
        this.escola = "Sombrio";
    }
    
    @Override
    public double calcularDano(String tipoFeitico) {
        double dano = poderBase;
        
        // Bônus para feitiços canalizados
        if (tipoFeitico.equals("CANALIZADO")) {
            dano *= 1.2; // +20% de dano
        }
        
        return dano;
    }
    
    @Override
    public double calcularCustoMana(String tipoFeitico) {
        double custoBase = 20;
        
        // Desconto para feitiços canalizados
        if (tipoFeitico.equals("CANALIZADO")) {
            return custoBase * 0.8; // -20% de mana
        }
        
        return custoBase;
    }
    
    // Método específico para magos sombrios
    public int drenarMana(Mago alvo, int quantidade) {
        int manaDrenada = Math.min(quantidade, alvo.getManaAtual());
        alvo.consumirMana(manaDrenada);
        this.restaurarMana(manaDrenada / 2);
        return manaDrenada;
    }

    
    private void restaurarMana(int quantidade) {
        this.manaAtual = Math.min(this.manaMax, this.manaAtual + quantidade);
    }
}
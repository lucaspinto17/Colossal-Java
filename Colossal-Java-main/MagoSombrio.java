public class MagoSombrio extends Mago {
    public MagoSombrio(int id, String codinome, int manaMax, double poderBase, double resistencia, String foco, String controlador, String perfilIA) {
        super(id, codinome, manaMax, poderBase, resistencia, foco, controlador, perfilIA);
        this.escola = "Sombrio";
    }

    @Override
    protected void inicializarGrimorio() {
        grimorio.add(new Feitico("Toque Vampírico", Feitico.PROJETIL, 15, 18.0));
        grimorio.add(new Feitico("Seta Sombria", Feitico.PROJETIL, 28, 35.0));
        grimorio.add(new Feitico("Agonia", Feitico.PROJETIL, 20, 10.0, EfeitoStatus.MANA_BURN, 3, 0.75));
        grimorio.add(new Feitico("Peste Sombria", Feitico.AREA, 45, 15.0, EfeitoStatus.MANA_BURN, 2, 0.40));
        grimorio.add(new Feitico("Toque do Vazio", Feitico.PROJETIL, 22, 10.0, EfeitoStatus.SILENCIO, 1, 0.60));
    }
}
/*
================================== FUNCIONALIDADES DA CLASSE ==================================
- Representa um Mago da escola Sombria, herdando todas as características da classe Mago.
- Define a sua 'escola' e preenche o grimório com feitiços sombrios, especializados em
  efeitos negativos ao longo do tempo, como Queima de Mana (MANA_BURN).
=============================================================================================
*/
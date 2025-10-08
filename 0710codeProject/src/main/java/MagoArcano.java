public class MagoArcano extends Mago {
    public MagoArcano(int id, String codinome, int manaMax, double poderBase, double resistencia, String foco, String controlador, String perfilIA) {
        super(id, codinome, manaMax, poderBase, resistencia, foco, controlador, perfilIA);
        this.escola = "Arcano";
    }

    @Override
    protected void inicializarGrimorio() {
        grimorio.add(new Feitico("Míssil Mágico", Feitico.PROJETIL, 15, 25.0));
        grimorio.add(new Feitico("Raio Arcano", Feitico.PROJETIL, 30, 40.0));
        grimorio.add(new Feitico("Nova Arcana", Feitico.AREA, 40, 20.0));
        grimorio.add(new Feitico("Impacto Rúnico", Feitico.PROJETIL, 25, 15.0, EfeitoStatus.ATORDOAMENTO, 1, 0.30));
        grimorio.add(new Feitico("Selo de Silêncio", Feitico.PROJETIL, 20, 5.0, EfeitoStatus.SILENCIO, 2, 0.50));
    }
}
/*
================================== FUNCIONALIDADES DA CLASSE ==================================
- Representa um Mago da escola Arcana, herdando todas as características da classe Mago.
- A sua única responsabilidade é definir a sua 'escola' e preencher o seu grimório inicial com
  feitiços arcanos, focados em dano direto e controlo (Atordoamento, Silêncio).
=============================================================================================
*/
public class MagoElemental extends Mago {
    public MagoElemental(int id, String codinome, int manaMax, double poderBase, double resistencia, String foco, String controlador, String perfilIA) {
        super(id, codinome, manaMax, poderBase, resistencia, foco, controlador, perfilIA);
        this.escola = "Elemental";
    }

    @Override
    protected void inicializarGrimorio() {
        grimorio.add(new Feitico("Bola de Fogo", Feitico.PROJETIL, 20, 22.0));
        grimorio.add(new Feitico("Lança de Gelo", Feitico.PROJETIL, 25, 30.0));
        grimorio.add(new Feitico("Vento Cortante", Feitico.AREA, 35, 18.0, EfeitoStatus.SILENCIO, 2, 0.25));
        grimorio.add(new Feitico("Terremoto", Feitico.AREA, 50, 45.0));
        grimorio.add(new Feitico("Relâmpago", Feitico.PROJETIL, 22, 18.0, EfeitoStatus.ATORDOAMENTO, 1, 0.20));
    }
}
/*
================================== FUNCIONALIDADES DA CLASSE ==================================
- Representa um Mago da escola Elemental, herdando todas as características da classe Mago.
- A sua principal responsabilidade é definir a 'escola' e preencher o grimório com feitiços
  elementais, com forte ênfase em dano em área (Terremoto) e controlo.
=============================================================================================
*/
/**
 * Representa um efeito ativo (como Atordoamento) num mago, com sua duração.
 */
public class EfeitoStatus {
    // --- Constantes de Tipos de Status ---
    public static final String SILENCIO = "Silêncio";
    public static final String ATORDOAMENTO = "Atordoamento";
    public static final String MANA_BURN = "Queima de Mana";

    private String tipo;
    private int duracao;

    public EfeitoStatus(String tipo, int duracao) {
        this.tipo = tipo;
        this.duracao = duracao;
    }

    /**
     * A cada turno, a duração do efeito diminui.
     * @return true se o efeito acabou, false caso contrário.
     */
    public boolean decrementarDuracao() {
        this.duracao--;
        return this.duracao <= 0;
    }

    public String getTipo() { return tipo; }
}
/*
================================== FUNCIONALIDADES DA CLASSE ==================================
- Define os nomes de todos os Status possíveis (SILENCIO, ATORDOAMENTO, MANA_BURN) como constantes.
- Modela um efeito de status ativo, guardando o seu 'tipo' e a sua 'duração' em turnos.
- Contém o método 'decrementarDuracao' que é chamado a cada rodada para reduzir o tempo do efeito
  até que ele desapareça.
=============================================================================================
*/
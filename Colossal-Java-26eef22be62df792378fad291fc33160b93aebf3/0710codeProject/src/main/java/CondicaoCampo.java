/**
 * Define as Condições de Campo que podem afetar um Duelo.
 * Cada condição aplica modificadores temporários que podem mudar a cada rodada.
 */
public final class CondicaoCampo {
    public static final String TEMPESTADE_ARCANA = "Tempestade Arcana";
    public static final String CHUVA_ELEMENTAL = "Chuva Elemental";
    public static final String NUVEM_SOMBRIA = "Nuvem Sombria";
    public static final String NORMAL = "Condições Normais"; // Estado neutro

    private CondicaoCampo() {
        // Construtor privado para impedir a instanciação
    }
}
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para GerenciadorMagos.
 * O objetivo é validar as regras de negócio da classe, como o cadastro,
 * a busca e a validação do limite de magos.
 */
class GerenciadorMagosTest {

    private GerenciadorMagos gerenciador;

    /**
     * Este método, anotado com @BeforeEach, é executado ANTES de cada teste.
     * A sua função é garantir que cada teste comece com um "ambiente limpo",
     * ou seja, uma nova instância do GerenciadorMagos. Isto evita que um teste
     * interfira com o resultado do outro.
     */
    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorMagos();
    }
    // --- CENÁRIO 1: Teste de Cadastro com Sucesso ---
    @Test
    void cadastrarMagoDeveAdicionarMagoNaListaEretornarTrue() {
        // 1. PREPARAÇÃO (Arrange)
        // Criamos um mago que será usado apenas neste teste.
        Mago novoMago = new MagoArcano(100, "Mago de Teste", 100, 10, 10, Mago.VARA, Mago.HUMANO, null);
        int quantidadeInicial = gerenciador.getQuantidadeMagos();

        // 2. AÇÃO (Act)
        // Executamos o método que queremos testar.
        boolean resultado = gerenciador.cadastrarMago(novoMago);

        // 3. VERIFICAÇÃO (Assert)
        // Verificamos se todas as condições esperadas foram cumpridas.
        assertTrue(resultado, "O método deveria retornar true para um cadastro bem-sucedido.");
        assertEquals(quantidadeInicial + 1, gerenciador.getQuantidadeMagos(),
                "A quantidade de magos na lista deveria ter aumentado em 1.");

        // Verificamos se o mago que acabamos de adicionar pode ser encontrado na lista.
        Mago magoEncontrado = gerenciador.buscarMagoPorId(100);
        assertNotNull(magoEncontrado, "O mago recém-cadastrado não foi encontrado na lista.");
        assertEquals("Mago de Teste", magoEncontrado.getCodinome(),
                "O codinome do mago encontrado não corresponde ao que foi cadastrado.");
    }

    // --- CENÁRIO 2 e 3: Testes de Busca ---
    @Test
    void buscarMagoPorIdDeveRetornarOMagoCorretoQuandoEleExiste() {
        // PREPARAÇÃO
        Mago magoAlvo = new MagoSombrio(42, "Alvo Certo", 100, 10, 10, Mago.TOMO, Mago.HUMANO, null);
        gerenciador.cadastrarMago(magoAlvo);

        // AÇÃO
        Mago magoEncontrado = gerenciador.buscarMagoPorId(42);

        // VERIFICAÇÃO
        assertEquals(magoAlvo, magoEncontrado, "O mago retornado não é o mesmo que foi cadastrado.");
    }

    @Test
    void buscarMagoPorIdDeveRetornarNullQuandoMagoNaoExiste() {
        // PREPARAÇÃO
        // Nenhuma preparação extra é necessária, o gerenciador está vazio.

        // AÇÃO
        Mago magoEncontrado = gerenciador.buscarMagoPorId(999);

        // VERIFICAÇÃO
        assertNull(magoEncontrado, "Deveria retornar null ao buscar por um ID que não existe.");
    }

    // --- CENÁRIO 4: Teste do Limite de Magos (Caso de Borda) ---
    @Test
    void cadastrarMagoDeveRetornarFalseQuandoLimiteForAtingido() {
        // PREPARAÇÃO
        // Enchemos o gerenciador com o número máximo de magos (12).
        for (int i = 1; i <= 12; i++) {
            gerenciador.cadastrarMago(new MagoElemental(i, "Mago " + i, 100, 10, 10, Mago.CAJADO, Mago.HUMANO, null));
        }

        // Verificamos se o estado inicial está correto.
        assertEquals(12, gerenciador.getQuantidadeMagos(), "A preparação falhou, o gerenciador deveria ter 12 magos.");

        // Criamos o 13º mago que tentará exceder o limite.
        Mago magoExtra = new MagoArcano(13, "Mago Extra", 100, 10, 10, Mago.VARA, Mago.HUMANO, null);

        // AÇÃO
        boolean resultado = gerenciador.cadastrarMago(magoExtra);

        // VERIFICAÇÃO
        assertFalse(resultado, "O cadastro deveria falhar e retornar false ao atingir o limite.");
        assertEquals(12, gerenciador.getQuantidadeMagos(),
                "A quantidade de magos não deveria aumentar após a tentativa de cadastro falhar.");
        assertNull(gerenciador.buscarMagoPorId(13), "O mago extra não deveria ter sido adicionado à lista.");
    }
}
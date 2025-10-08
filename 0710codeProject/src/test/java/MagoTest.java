// Importa as ferramentas do JUnit que vamos usar
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para a classe Mago.
 * Cada método anotado com @Test é um teste unitário isolado.
 */
class MagoTest {

    // --- TESTE 1: Dano Normal ---
    @Test // Marca este método como um teste executável
    void magoDevePerderAVidaCorretamenteAoReceberDano() {
        // 1. PREPARAÇÃO (Arrange)
        // Criamos um mago específico para este cenário de teste.
        Mago magoDeTeste = new MagoArcano(1, "Teste Dano", 100, 10, 10, Mago.VARA, Mago.HUMANO, null);
        int vidaInicial = magoDeTeste.getVidaAtual();
        double danoSofrido = 25.0;
        int vidaEsperada = vidaInicial - (int)danoSofrido;

        // 2. AÇÃO (Act)
        // Executamos o método que queremos testar.
        magoDeTeste.receberDano(danoSofrido);

        // 3. VERIFICAÇÃO (Assert)
        // Verificamos se o resultado da ação é o que esperávamos.
        // O teste só passa se a vida atual for igual à vida esperada.
        assertEquals(vidaEsperada, magoDeTeste.getVidaAtual(), "O cálculo de dano está incorreto.");
    }

    // --- TESTE 2: Dano Excessivo (Caso de Borda) ---
    @Test
    void vidaDoMagoNaoDeveFicarNegativaAposDanoMassivo() {
        // 1. PREPARAÇÃO
        Mago magoDeTeste = new MagoSombrio(2, "Teste Sobrevivência", 100, 10, 10, Mago.TOMO, Mago.HUMANO, null);
        double danoMassivo = 9999.0;

        // 2. AÇÃO
        magoDeTeste.receberDano(danoMassivo);

        // 3. VERIFICAÇÃO
        // Garantimos que a vida não fica negativa, deve parar em 0.
        assertEquals(0, magoDeTeste.getVidaAtual(), "A vida do mago ficou com um valor negativo.");
    }

    // --- TESTE 3: Verificação de Estado ---
    @Test
    void metodoEstaVivoDeveRetornarFalseQuandoAVidaForZero() {
        // 1. PREPARAÇÃO
        Mago magoDeTeste = new MagoElemental(3, "Teste Abate", 100, 10, 10, Mago.CAJADO, Mago.HUMANO, null);

        // 2. AÇÃO
        magoDeTeste.receberDano(magoDeTeste.getVidaMax()); // Dano exato para zerar a vida

        // 3. VERIFICAÇÃO
        // Verificamos se o método booleano retorna o valor correto.
        assertFalse(magoDeTeste.estaVivo(), "O método estaVivo() não retornou false com a vida em 0.");
        assertTrue(magoDeTeste.getVidaAtual() == 0, "A vida do mago não foi zerada corretamente.");
    }
}

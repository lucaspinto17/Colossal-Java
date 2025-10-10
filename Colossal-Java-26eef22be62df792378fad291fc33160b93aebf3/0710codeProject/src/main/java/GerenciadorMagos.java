import java.util.ArrayList;
import java.util.List;

public class GerenciadorMagos {
    private List<Mago> magos = new ArrayList<>();
   
    private final int LIMITE_MAGOS = 12;

    public boolean cadastrarMago(Mago mago) {
        if (magos.size() >= LIMITE_MAGOS) {
            System.out.println("Erro: Limite de magos na arena foi atingido!");
            return false;
        }
        magos.add(mago);
       
        
        return true;
    }

    public Mago buscarMagoPorId(int id) {
        for (Mago mago : magos) {
            if (mago.getId() == id) {
                return mago;
            }
        }
        return null;
    }

    public void listarMagos() {
        System.out.println("\n=== LISTA DE MAGOS CADASTRADOS ===");
        if (magos.isEmpty()) {
            System.out.println("Nenhum mago cadastrado.");
        } else {
            for (Mago mago : magos) {
                System.out.println(mago);
            }
        }
    }

    public int getQuantidadeMagos() {
        return magos.size();
    }

    public List<Mago> getMagos() {
        return new ArrayList<>(this.magos);
    }
}
/*
 * ================================== FUNCIONALIDADES DA CLASSE
 * ==================================
 * - Atua como um "banco de dados" em memória para todos os Magos registados.
 * - Controla o registo de novos magos, respeitando o limite máximo de 12.
 * - Permite buscar um mago específico pelo seu ID.
 * - Oferece métodos para listar todos os magos e obter a lista completa.
 * =============================================================================
 * ================
 */
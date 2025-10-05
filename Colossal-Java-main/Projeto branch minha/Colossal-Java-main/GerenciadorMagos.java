import java.util.ArrayList;
import java.util.List;

public class GerenciadorMagos {
    private List<Mago> magos;
    private final int LIMITE_MAGOS = 12;
    
    public GerenciadorMagos() {
        this.magos = new ArrayList<>();
    }
    
    public boolean cadastrarMago(Mago mago) {
        if (magos.size() >= LIMITE_MAGOS) {
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
        System.out.println("\n=== LISTA DE MAGOS ===");
        if (magos.isEmpty()) {
            System.out.println("Nenhum mago cadastrado.");
        } else {
            for (Mago mago : magos) {
                System.out.println(mago);
            }
        }
        System.out.println("Total: " + magos.size() + "/" + LIMITE_MAGOS);
    }
    
    public List<Mago> getMagos() {
        return new ArrayList<>(magos);
    }
    
    public int getQuantidadeMagos() {
        return magos.size();
    }
}
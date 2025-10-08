import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    /**
     * A URL de conexão JDBC. É uma String que informa ao DriverManager como se
     * conectar.
     * - "jdbc:sqlite:": Especifica que estamos usando o driver para um banco de
     * dados SQLite.
     * - "C:/.../coliseu.db": É o caminho absoluto para o arquivo do banco de dados.
     * Usar um caminho absoluto foi uma ótima decisão para depurar e garantir que
     * o programa sempre acesse o mesmo arquivo, independentemente de onde ele é
     * executado.
     */
    private static final String URL = "jdbc:sqlite:C:/Users/adiss/OneDrive/Documentos/ProjetoRPG/0710codeProject/coliseu.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inicializarBanco() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS magos (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            classe TEXT,
                            codinome TEXT,
                            escola TEXT,
                            foco TEXT,
                            controlador TEXT,
                            perfilIA TEXT,
                            manaMax INTEGER,
                            vidaMax INTEGER,
                            poderBase REAL,
                            resistenciaMagica REAL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS feiticos (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            nome TEXT,
                            elemento TEXT,
                            custoMana INTEGER,
                            poder REAL,
                            mago_id INTEGER,
                            FOREIGN KEY (mago_id) REFERENCES magos(id)
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS duelos (
                            id INTEGER PRIMARY KEY,
                            modo TEXT,
                            arena TEXT,
                            dataHora TEXT,
                            finalizado INTEGER,
                            vencedor TEXT
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS duelo_magos (
                            duelo_id INTEGER,
                            mago_id INTEGER,
                            equipe INTEGER,
                            FOREIGN KEY (duelo_id) REFERENCES duelos(id),
                            FOREIGN KEY (mago_id) REFERENCES magos(id)
                        )
                    """);
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS duelo_estatisticas (
                            duelo_id INTEGER NOT NULL,
                            mago_id INTEGER NOT NULL,
                            dano_causado REAL DEFAULT 0,
                            dano_mitigado REAL DEFAULT 0,
                            abates INTEGER DEFAULT 0,
                            assistencias INTEGER DEFAULT 0,
                            FOREIGN KEY (duelo_id) REFERENCES duelos(id),
                            FOREIGN KEY (mago_id) REFERENCES magos(id),
                            PRIMARY KEY (duelo_id, mago_id)
                        )
                    """);

            System.out.println("Banco de dados inicializado.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

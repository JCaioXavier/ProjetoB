package BancoDeDados;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class CriptografiaDAO {
    public static Map<String, String> Criptografia() {
        Scanner scanner = new Scanner(System.in);

        String senhaProvisoria = null, senhaCriptografada = null, senha;

        do {
            System.out.print("Digite sua senha: ");
            senha = scanner.nextLine();

            while (senha.length() > 50) {
                System.out.println("[ERROR] Senha não pode ser maior que 50 caracteres.");
                System.out.print("Digite sua senha: ");
                senha = scanner.nextLine();
            }
            if (senha.isEmpty()) {
                System.out.println("[ERROR]Senha não pode ser vazio ou apenas espaços!");
            }
        } while (senha.isEmpty());

        try {
            senhaProvisoria = Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(senha.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            senhaCriptografada = Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(senhaProvisoria.getBytes(StandardCharsets.UTF_8))
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> resultado = new HashMap<>();
        resultado.put("senha", senha);
        resultado.put("SenhaCriptografada", senhaCriptografada);

        return resultado;
    }
}

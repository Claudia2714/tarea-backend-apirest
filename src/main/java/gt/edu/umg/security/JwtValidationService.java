package gt.edu.umg.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class JwtValidationService {

    // 1. Método para leer y preparar la llave pública RSA
    private PublicKey getPublicKey() throws Exception {
        // Busca el archivo public.pem en la carpeta resources
        InputStream inputStream = new ClassPathResource("public.pem").getInputStream();
        String keyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        // Limpia el texto eliminando encabezados, pies de página y espacios
        String publicKeyPEM = keyString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        // Decodifica y genera el objeto PublicKey de Java
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
    }

    // 2. Método para validar el token y extraer el usuario
    public String validateTokenAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getPublicKey()) // ¡Aquí usa la llave pública para verificar la firma!
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getSubject(); // Devuelve el "username" (ej. admin) si el token es válido
        } catch (Exception e) {
            // Si la firma no coincide, el token expiró o está mal formado, falla la validación
            System.out.println("Error validando el token: " + e.getMessage());
            return null;
        }
    }
}
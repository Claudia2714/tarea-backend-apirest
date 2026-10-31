package gt.edu.umg.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    /** Orígenes permitidos. En producción inyectar variable de entorno ALLOWED_ORIGINS */
    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private String allowedOrigins;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // ─── Bean de configuración CORS global ───────────────────────────────────
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Soporta múltiples orígenes separados por coma desde la variable de entorno
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // ─── Cadena de filtros de seguridad ──────────────────────────────────────
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Sin estado: el servidor no guarda sesión (modo Resource Server JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 2. Deshabilitar CSRF (no aplica en APIs REST sin estado)
            .csrf(csrf -> csrf.disable())

            // 3. Habilitar CORS con la configuración del bean de arriba
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 4. Deshabilitar formulario de login y Basic Auth
            //    → esto elimina el mensaje "Using generated security password"
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // 5. Reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // Swagger/OpenAPI públicos (útil durante el desarrollo)
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Todas las demás rutas requieren token JWT válido
                .anyRequest().authenticated()
            )

            // 6. Filtro JWT personalizado, se ejecuta antes del filtro estándar
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


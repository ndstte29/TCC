package tg.codigo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/usuarios/login", 
                    "/usuarios/novo", 
                    "/usuarios/solicitar-redefinicao", 
                    "/usuarios/redefinir-senha", 
                    "/usuarios/esqueci-senha", 
                    "/css/**", "/js/**", "/img/**"  // libera recursos estáticos
                ).permitAll()
                .anyRequest().authenticated() // Requer login para todas as outras rotas
            )
            .formLogin(form -> form
                .loginPage("/usuarios/login")  // Página de login personalizada
                .loginProcessingUrl("/usuarios/login")  // URL onde o login é processado
                .defaultSuccessUrl("/index", true)  // Redireciona para a página principal após sucesso
                .failureUrl("/usuarios/login?error=true")  // Redireciona em caso de erro
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // URL para logout
                .logoutSuccessUrl("/usuarios/login?logout=true")  // Redireciona após logout
                .permitAll()
            );

        return http.build();
    }
}

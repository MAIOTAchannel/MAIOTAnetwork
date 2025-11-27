package work.maiotanet.MAIOTANetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/**").authenticated() // /admin配下は認証が必要
                    .anyRequest().permitAll()                     // それ以外は誰でもアクセス可能
            )
            .formLogin(login -> login
                    .defaultSuccessUrl("/admin", true) // ログイン成功時は管理画面へ
                    .permitAll()
            )
            .logout(logout -> logout.permitAll());

    return http.build();
  }

  // 簡易的なインメモリユーザー（本番ではDB管理に変えてください）
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {
    UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password") // 強力なパスワードに変更してください
            .roles("ADMIN")
            .build();
    return new InMemoryUserDetailsManager(admin);
  }
}
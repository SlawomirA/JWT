package thesis.backend.jwt.model.MySQL;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import thesis.backend.jwt.enums.TokenType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  public Integer id;

  @Column(name = "token", unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  @Column(name = "token_type", unique = true)
  public TokenType tokenType = TokenType.BEARER;

  @Column(name = "revoked", unique = true, columnDefinition = "BIT")
  public boolean revoked;

  @Column(name = "expired", unique = true, columnDefinition = "BIT")
  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}

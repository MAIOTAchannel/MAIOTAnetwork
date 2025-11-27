package work.maiotanet.MAIOTANetwork.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  // 長文を保存するためTEXT型を指定
  @Column(columnDefinition = "TEXT")
  private String content;

  private LocalDateTime createdAt;

  // 公開/非公開フラグ
  private boolean isPublished;

  @PrePersist
  public void onPrePersist() {
    this.createdAt = LocalDateTime.now();
  }
}
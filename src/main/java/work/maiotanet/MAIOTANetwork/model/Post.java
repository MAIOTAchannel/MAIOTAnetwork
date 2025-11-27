package work.maiotanet.MAIOTANetwork.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title is required")
  private String title;

  // 長文を保存するためTEXT型を指定
  @Column(columnDefinition = "TEXT")
  @NotBlank(message = "Content is required")
  private String content;

  private LocalDateTime createdAt;

  // 公開/非公開フラグ
  private boolean isPublished;

  @PrePersist
  public void onPrePersist() {
    this.createdAt = LocalDateTime.now();
  }
}
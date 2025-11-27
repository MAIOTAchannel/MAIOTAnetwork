package work.maiotanet.MAIOTANetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import work.maiotanet.MAIOTANetwork.model.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  // 公開済み記事のみを新しい順に取得するメソッド
  List<Post> findByIsPublishedTrueOrderByCreatedAtDesc();

  // 管理画面用：全記事を新しい順に
  List<Post> findAllByOrderByCreatedAtDesc();
}
package work.maiotanet.MAIOTANetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import work.maiotanet.MAIOTANetwork.model.Post;
import work.maiotanet.MAIOTANetwork.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class BlogController {

  private final PostRepository postRepository;

  public BlogController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  // 公開ブログ一覧
  @GetMapping("/blog")
  public String blogIndex(Model model) {
    model.addAttribute("posts", postRepository.findByIsPublishedTrueOrderByCreatedAtDesc());
    return "blog/index";
  }

  // 記事詳細
  @GetMapping("/blog/{id}")
  public String blogDetail(@PathVariable Long id, Model model) {
    Post post = postRepository.findById(id).orElseThrow();
    model.addAttribute("post", post);
    return "blog/detail";
  }

  // --- 以下、CMS（管理機能） ---

  // 管理画面トップ（記事一覧）
  @GetMapping("/admin")
  public String adminIndex(Model model) {
    model.addAttribute("posts", postRepository.findAllByOrderByCreatedAtDesc());
    return "admin/index";
  }

  // 記事作成フォーム
  @GetMapping("/admin/post/new")
  public String newPostForm(Model model) {
    model.addAttribute("post", new Post());
    return "admin/editor";
  }

  // 記事保存処理
  @PostMapping("/admin/post/save")
  public String savePost(@ModelAttribute @Valid Post post, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "admin/editor";
    }
    postRepository.save(post);
    return "redirect:/admin";
  }

  // 記事削除処理
  @GetMapping("/admin/post/delete/{id}")
  public String deletePost(@PathVariable Long id) {
    postRepository.deleteById(id);
    return "redirect:/admin";
  }

  // 記事編集フォーム（既存の記事データを取得して表示）
  @GetMapping("/admin/post/edit/{id}")
  public String editPostForm(@PathVariable Long id, Model model) {
    Post post = postRepository.findById(id).orElseThrow();
    model.addAttribute("post", post);
    return "admin/editor"; // 新規作成と同じテンプレートを再利用
  }
}
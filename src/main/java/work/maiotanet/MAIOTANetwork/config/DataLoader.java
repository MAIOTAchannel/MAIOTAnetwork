package work.maiotanet.MAIOTANetwork.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import work.maiotanet.MAIOTANetwork.model.Post;
import work.maiotanet.MAIOTANetwork.repository.PostRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final PostRepository postRepository;

    public DataLoader(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            Post post = new Post();
            post.setTitle("Welcome to the New Blog Editor!");
            post.setContent("""
                    # Hello World!

                    This is a sample post to demonstrate the new **Markdown** features.

                    ## Features
                    - **Bold** and *Italic* text
                    - Lists:
                      1. Item 1
                      2. Item 2
                    - [Links](https://example.com)
                    - Code blocks:
                      ```java
                      System.out.println("Hello");
                      ```

                    Enjoy writing!
                                """);
            post.setPublished(true);
            postRepository.save(post);
        }
    }
}

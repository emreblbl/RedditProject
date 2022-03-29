package com.example.demo.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.Instant;

@Data // ?>>
@Entity
@Builder // ? >>
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long postId;
    @NotBlank(message = "Post name cannot be empty or NULL")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount;
    @ManyToOne(fetch = FetchType.LAZY) // >> ??
    @JoinColumn(name = "userId",referencedColumnName = "userId")
    private User user;
    private Instant createdDate; // ??? when post is created
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Subreddit subreddit;


}

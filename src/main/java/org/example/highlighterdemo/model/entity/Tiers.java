package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
///     tiers entity...
///     member 와 n:1 연관관계이다.
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tiers")
@Schema(description = "시즌 별 티어")
public class Tiers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "tiers pk")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "시즌")
    private String season;

    @Column
    @Schema(description = "티어")
    private String tier;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Schema(description = "유저")
    private Member member;
}

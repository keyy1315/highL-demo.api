package org.example.highlighterdemo.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "tag")
@Schema(description = "tag entity")
public class Tag {
    @Id
    @Schema(description = "tag pk")
    private String Id;

    @Column
    @Schema(description = "tag name")
    private String name;

    public static Tag create(String tagName) {
        return Tag.builder()
                .Id(UUID.randomUUID().toString())
                .name(tagName)
                .build();
    }
}

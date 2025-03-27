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

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "category")
@Schema(description = "category entity")
public class Category {
    @Id
    @Schema(description = "category_pk")
    private String id;

    @Column
    @Schema(description = "category name")
    private String name;
}

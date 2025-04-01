package org.example.highlighterdemo.service;

import lombok.RequiredArgsConstructor;
import org.example.highlighterdemo.model.entity.Tag;
import org.example.highlighterdemo.repository.tag.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> setTags(List<String> tags) {
        return tags.stream()
                .map(tagName -> {
                    Optional<Tag> existingTag = tagRepository.findByName(tagName);
                    return existingTag.orElseGet(() -> {
                        Tag newTag = Tag.create(tagName);
                        return tagRepository.save(newTag);
                    });
                })
                .collect(Collectors.toList());
    }
}

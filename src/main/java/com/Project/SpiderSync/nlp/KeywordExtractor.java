package com.Project.SpiderSync.nlp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeywordExtractor {

  private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
      "a", "an", "and", "are", "as", "at", "be", "by", "for", "from",
      "has", "he", "in", "is", "it", "its", "of", "on", "that", "the",
      "to", "was", "will", "with", "the", "this", "but", "they", "have",
      "had", "what", "when", "where", "who", "which", "why", "how"));

  /**
   * Extract keywords using simple TF-IDF approach
   */
  public String extractKeywords(String title, String content, int maxKeywords) {
    if (content == null || content.isEmpty()) {
      return "";
    }

    // Combine title and content, giving more weight to title
    String text = (title != null ? title + " " + title + " " : "") + content;

    // Tokenize and clean
    String[] words = text.toLowerCase()
        .replaceAll("[^a-z0-9\\s]", " ")
        .split("\\s+");

    // Count word frequencies
    Map<String, Integer> wordFreq = new HashMap<>();
    for (String word : words) {
      if (word.length() > 3 && !STOP_WORDS.contains(word)) {
        wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
      }
    }

    // Sort by frequency and get top keywords
    List<String> keywords = wordFreq.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .limit(maxKeywords)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

    return String.join(", ", keywords);
  }

  /**
   * Calculate simple relevance score based on keyword density
   */
  public double calculateRelevanceScore(String content, String query) {
    if (content == null || query == null) {
      return 0.0;
    }

    String[] queryWords = query.toLowerCase().split("\\s+");
    String contentLower = content.toLowerCase();

    int matches = 0;
    for (String word : queryWords) {
      if (contentLower.contains(word)) {
        matches++;
      }
    }

    return (double) matches / queryWords.length;
  }
}

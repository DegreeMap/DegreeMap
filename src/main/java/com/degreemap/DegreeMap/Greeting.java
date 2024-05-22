package com.degreemap.DegreeMap;

public record Greeting(long id, String content) { 
    public Greeting {
        if (content == null || content.isBlank()) {
          throw new IllegalArgumentException("Message cannot be null or blank");
        }
      }

      public long getId() {
        return id;
      }

      public String getContent() {
        return content;
      }
}


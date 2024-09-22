package com.ex.restapipractice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore    // 순환참조 방지. 그러나 안티패턴이다. entity 가 응답으로 바로 나가기 때문
  private User user;
}

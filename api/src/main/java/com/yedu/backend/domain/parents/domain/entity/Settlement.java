package com.yedu.backend.domain.parents.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Settlement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long settlementId;

  @ManyToOne(fetch = FetchType.LAZY)
  private Parents parents;

  private String receipt;
  private String bank;
  private String accountNumber;
  private String accountName;
}

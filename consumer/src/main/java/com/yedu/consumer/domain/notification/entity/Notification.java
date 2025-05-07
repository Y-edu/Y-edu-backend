package com.yedu.consumer.domain.notification.entity;

import com.yedu.consumer.domain.notification.type.PushType;
import com.yedu.consumer.domain.notification.type.ReceiverType;
import com.yedu.consumer.domain.notification.type.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private PushType pushType;

  @Enumerated(EnumType.STRING)
  private ReceiverType receiverType;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String receiverPhoneNumber;

  private String content;

  private String clientKey; // API 클라이언트 생성키

  private String serverKey; // API 서버 생성키

  private LocalDateTime consumedAt;

  private LocalDateTime deliveredAt; // 사용자 수신 시간

  public void success(String serverKey) {
    this.serverKey = serverKey;
    this.status = Status.SUCCESS;
  }

  public void fail() {
    this.status = Status.FAIL;
  }

  public void successDelivery() {
    this.status = Status.DELIVERY_SUCCESS;
    this.deliveredAt = LocalDateTime.now();
  }

  public void failDelivery() {
    this.status = Status.DELIVERY_FAIL;
    this.deliveredAt = LocalDateTime.now();
  }
}

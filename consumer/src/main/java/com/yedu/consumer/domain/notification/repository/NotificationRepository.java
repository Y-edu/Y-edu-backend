package com.yedu.consumer.domain.notification.repository;

import com.yedu.consumer.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Optional<Notification> findByServerKeyAndClientKey(String serverKey, String clientKey);

  List<Notification> findByTemplateCodeAndReceiverPhoneNumberAndDeliveredAtAfter(
      String templateCode, String receiverPhoneNumber, LocalDateTime deliveredAtAfter);
}

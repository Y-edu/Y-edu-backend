package com.yedu.consumer.domain.notification.repository;

import com.yedu.consumer.domain.notification.entity.Notification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Optional<Notification> findByServerKeyAndClientKey(String serverKey, String clientKey);
}

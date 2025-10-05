package com.management.building.entity.device;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {
        @Index(name = "idx_id_deviceId_statusCode", columnList = "id, device_id, status_code"),
        @Index(name = "idx_updatedAt", columnList = "updated_at"),
        @Index(name = "idx_eventTime", columnList = "event_time") })
public class DeviceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "device_id", nullable = false)
    Device device;

    @Column(name = "status_code", nullable = false)
    String statusCode;

    String value;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "event_time", nullable = false)
    Long eventTime;

}

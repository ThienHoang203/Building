package com.management.building.entity.device;

import java.time.LocalDateTime;

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
        @Index(name = "idx_id_deviceId_statusCode_eventTime_recordedAt", columnList = "id, device_id, status_code, event_time, recorded_at"),
        @Index(name = "idx_deviceId_eventTime_recordedAt", columnList = "device_id, event_time, recorded_at"),
        @Index(name = "idx_batchId", columnList = "batch_id") })
public class DeviceStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    Device device;

    @Column(name = "status_code", nullable = false)
    String statusCode;

    @Column(nullable = true, columnDefinition = "NVARCHAR(500)")
    String value;

    @Column(name = "event_time", nullable = true)
    Long eventTime;

    @Column(name = "recorded_at", nullable = false)
    LocalDateTime recordedAt;

    @Column(name = "batch_id", length = 36)
    String batchId;

}

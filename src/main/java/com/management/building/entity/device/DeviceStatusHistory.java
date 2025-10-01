package com.management.building.entity.device;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(indexes = { @Index(unique = true, columnList = "recordedAt, changedAt") })
public class DeviceStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String value;
    LocalDateTime changedAt;
    LocalDateTime recordedAt;
    @ManyToOne
    @JoinColumn(name = "device_status_id", nullable = false)
    DeviceStatus deviceStatus;

}

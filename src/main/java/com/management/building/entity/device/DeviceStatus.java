package com.management.building.entity.device;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(indexes = { @Index(columnList = "changedAt") })
public class DeviceStatus {

    @Id
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    Device device;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code", nullable = false)
    CategoryStatus status;
    LocalDateTime changedAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deviceStatus", cascade = { CascadeType.REMOVE })
    Set<DeviceStatusHistory> deviceStatusHistories;

}

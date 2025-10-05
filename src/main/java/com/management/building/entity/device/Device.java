package com.management.building.entity.device;

import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.management.building.entity.space.Space;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_last_sync_time", columnList = "last_sync_time"),
        @Index(name = "idx_ip", columnList = "ip"),
        @Index(name = "idx_id_productId", columnList = "id, productId"),
        @Index(name = "idx_id_categoryCode", columnList = "id, category_code"),
        @Index(name = "idx_productId", columnList = "productId") })
public class Device {

    @Id
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_code", nullable = false)
    Supplier supplier;

    @Column(nullable = false)
    String productId;

    @Column(nullable = false)
    String ip;

    @Column(nullable = false)
    String lat;

    @Column(nullable = false)
    String lon;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Boolean isOnline;

    String customName;

    @Column(name = "last_sync_time")
    Long lastSyncTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Space space;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", cascade = { CascadeType.REMOVE }, orphanRemoval = true)
    Set<DeviceStatus> deviceStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", cascade = { CascadeType.REMOVE }, orphanRemoval = true)
    Set<DeviceStatusLog> deviceStatusLogs;

}

package com.management.building.entity.space;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;

import java.util.LinkedHashSet;
import java.util.Set;

import com.management.building.enums.space.SpaceFunction;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SpaceType {

  @Id
  String name;

  @Column(length = 1000)
  String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "function_type")
  @ToString.Exclude
  Set<SpaceFunction> functions;

  @Column(length = 500)
  String specifications;

  @Column(nullable = false)
  Boolean requiresSpecialAccess;

  @Min(value = 0)
  Integer maxCapacity;

  @Min(value = 0)
  @Column(name = "level", nullable = false)
  Integer level;

  @Column(name = "parent_space_id")
  String parentSpaceTypeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_space_type_id", insertable = false, updatable = false)
  @ToString.Exclude
  SpaceType parentSpaceType;

  @OneToMany(mappedBy = "parentSpaceType", cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
  @Builder.Default
  @ToString.Exclude
  Set<SpaceType> childSpaceTypes = new LinkedHashSet<>();

  @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
  @Builder.Default
  @ToString.Exclude
  Set<Space> spaces = new LinkedHashSet<>();

}

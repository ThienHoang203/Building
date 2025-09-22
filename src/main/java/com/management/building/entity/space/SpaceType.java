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

import java.util.Set;

import com.management.building.enums.space.SpaceFunction;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SpaceType {

  @Id
  @Column(length = 150)
  String name;

  @Column(nullable = false)
  Boolean requiresSpecialAccess;

  @Min(value = 0)
  @Column(name = "level", nullable = false)
  Integer level;

  @Column(length = 1000)
  String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "space_functions")
  Set<SpaceFunction> spaceFunctions;

  @Column(length = 500)
  String specifications;

  @Min(value = 0)
  Integer maxCapacity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_name", nullable = true)
  SpaceType parent;

  @OneToMany(mappedBy = "parent", cascade = { CascadeType.MERGE,
      CascadeType.PERSIST }, fetch = FetchType.LAZY)
  Set<SpaceType> children;

  @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
  Set<Space> spaces;
}

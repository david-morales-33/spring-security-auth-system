package com.auth.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_permission")
public class PermissionEntity {

    @Id
    @Column(name = "prm_id")
    private Integer id;

    @Column(name = "prm_name", nullable = false, updatable = false)
    private String name;

}

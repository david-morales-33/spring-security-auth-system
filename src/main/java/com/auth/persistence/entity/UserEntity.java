package com.auth.persistence.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class UserEntity {

    @Id
    @Column(name = "usr_id")
    private String id;

    @Column(name = "usr_name", length = 50, nullable = false, unique = true)
    private String userName;

    @Column(name = "usr_password", nullable = false)
    private String password;

    @Column(name = "usr_is_enable", nullable = false)
    private boolean isEnable;

    @Column(name = "usr_account_no_expired", nullable = false)
    private boolean accountNoExpired;

    @Column(name = "usr_account_no_lock", nullable = false)
    private boolean AccountNoLock;

    @Column(name = "usr_credential_no_expired", nullable = false)
    private boolean credentialNoExpired;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "tbl_user_x_role", joinColumns = @JoinColumn(name = "usr_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<RoleEntity> roleLis;
}

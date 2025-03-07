package com.auth.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.controllers.DTO.AuthLoginRequest;
import com.auth.controllers.DTO.AuthRegisterUserRequest;
import com.auth.controllers.DTO.AuthResponse;
import com.auth.persistence.entity.RoleEntity;
import com.auth.persistence.entity.UserEntity;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtils;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                UserEntity userEntity = userRepository
                                .findUserEntityByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();

                userEntity.getRoleLis().forEach(role -> simpleGrantedAuthorities
                                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

                userEntity.getRoleLis().stream()
                                .flatMap(role -> role.getPermissionList().stream())
                                .forEach(permission -> simpleGrantedAuthorities
                                                .add(new SimpleGrantedAuthority(permission.getName())));

                return new User(
                                userEntity.getUsername(),
                                userEntity.getPassword(),
                                userEntity.isEnable(),
                                userEntity.isAccountNoExpired(),
                                userEntity.isCredentialNoExpired(),
                                userEntity.isAccountNoLock(),
                                simpleGrantedAuthorities);
        }

        public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

                String username = authLoginRequest.username();
                String password = authLoginRequest.password();

                Authentication authentication = this.authenticate(username, password);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = this.jwtUtils.generateToken(authentication);

                AuthResponse authResponse = new AuthResponse(username, "success authentication", token, true);
                return authResponse;
        }

        public Authentication authenticate(String username, String password) {
                UserDetails userDetails = this.loadUserByUsername(username);

                if (userDetails == null) {
                        throw new BadCredentialsException("User not found");
                }
                if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
                        throw new BadCredentialsException("Password error");
                }
                return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                                userDetails.getAuthorities());
        }

        public AuthResponse registerUser(AuthRegisterUserRequest authRegisterUserRequest) {
                String username = authRegisterUserRequest.username();
                String password = authRegisterUserRequest.password();
                List<String> roleList = authRegisterUserRequest.roleList().roleList();
                Set<RoleEntity> roleEntitieList = this.roleRepository
                                .findRoleEntitiesByRoleEnumIn(roleList)
                                .stream()
                                .collect(Collectors.toSet());

                if (roleEntitieList.isEmpty()) {
                        throw new IllegalArgumentException("The role list not valid");
                }

                UserEntity userEntity = UserEntity
                                .builder()
                                .id(UUID.randomUUID().toString())
                                .username(username)
                                .password(passwordEncoder.encode(password))
                                .roleLis(roleEntitieList)
                                .isEnable(true)
                                .accountNoExpired(true)
                                .AccountNoLock(true)
                                .credentialNoExpired(true)
                                .build();
                UserEntity userCreated = this.userRepository.save(userEntity);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                userCreated.getRoleLis()
                                .forEach(role -> authorities.add(
                                                new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
                userCreated.getRoleLis()
                                .stream()
                                .flatMap(role -> role.getPermissionList().stream())
                                .forEach(permission -> authorities
                                                .add(new SimpleGrantedAuthority(permission.getName())));
                // SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(),
                                userCreated.getPassword(), authorities);
                String accessToken = jwtUtils.generateToken(authentication);

                AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "User create succes",
                                accessToken, true);

                return authResponse;
        }
}

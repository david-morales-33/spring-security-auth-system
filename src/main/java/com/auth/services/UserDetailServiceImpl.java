package com.auth.services;

import java.util.ArrayList;
import java.util.List;

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
import com.auth.controllers.DTO.AuthResponse;
import com.auth.persistence.entity.UserEntity;
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

                AuthResponse authResponse = new AuthResponse(username, password, token, true);
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
}

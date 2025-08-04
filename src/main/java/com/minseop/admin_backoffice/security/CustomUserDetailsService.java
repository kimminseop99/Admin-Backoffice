package com.minseop.admin_backoffice.security;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserStatus;
import com.minseop.admin_backoffice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다. username=" + username));

        if (user.getStatus() == UserStatus.BANNED) {
            throw new LockedException("BANNED:" + user.getUsername());
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new DisabledException("계정이 비활성화되어 있습니다.");
        }

        return new CustomUserDetails(user);
    }



}
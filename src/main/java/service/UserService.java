package service;

import domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;



public interface UserService {
    Page<UserEntity> findAllUsers(String searchKeyword, Pageable pageable);

    Optional<UserEntity> findById(Long id);

    UserEntity saveUser(UserEntity user);

    void deleteUser(Long id);
}

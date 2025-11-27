package com.poseidon.tradingapp.services;

import com.poseidon.tradingapp.domain.User;
import com.poseidon.tradingapp.dto.UserDto;
import com.poseidon.tradingapp.exceptions.UserNotFoundException;
import com.poseidon.tradingapp.mappers.UserMapper;
import com.poseidon.tradingapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================================
    // FIND ALL
    // ============================================================
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        log.info("{} utilisateur(s) trouvés.", users.size());
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    // ============================================================
    // FIND BY ID
    // ============================================================
    public UserDto findById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable avec id=" + id));

        return userMapper.toDto(user);
    }

    // ============================================================
    // CREATE
    // ============================================================
    public UserDto create(UserDto dto) {
        User entity = userMapper.toEntity(dto);

        // Hashage du mot de passe AVANT sauvegarde
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(entity);
        log.info("Utilisateur créé : id={}", saved.getUserId());

        return userMapper.toDto(saved);
    }

    // ============================================================
    // UPDATE
    // ============================================================
    public UserDto update(Integer id, UserDto dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Impossible de mettre à jour : utilisateur introuvable avec id=" + id));

        // Update via MapStruct
        userMapper.updateEntityFromDto(dto, existing);

        // Gestion du mot de passe
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            // Si un nouveau mot de passe a été saisi → re-hash
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            // Sinon → conserver l'ancien mot de passe
            log.debug("Mot de passe non modifié pour l'utilisateur id={}", id);
        }

        User updated = userRepository.save(existing);
        log.info("Utilisateur mis à jour : id={}", updated.getUserId());

        return userMapper.toDto(updated);
    }

    // ============================================================
    // DELETE
    // ============================================================
    public void delete(Integer id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Suppression impossible : utilisateur introuvable avec id=" + id));

        userRepository.delete(existing);
        log.info("Utilisateur supprimé id={}", id);
    }

}

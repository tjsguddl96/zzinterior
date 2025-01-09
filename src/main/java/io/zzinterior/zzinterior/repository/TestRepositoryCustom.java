package io.zzinterior.zzinterior.repository;

import io.zzinterior.zzinterior.entity.User;

import java.util.Optional;

public interface TestRepositoryCustom {
    Optional<User> findByEmail(String email);
}

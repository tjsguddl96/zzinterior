package io.zzinterior.zzinterior.repository;

import io.zzinterior.zzinterior.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TestRepository extends JpaRepository<User,Long>,TestRepositoryCustom {


}

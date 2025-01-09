package io.zzinterior.zzinterior;

import java.util.Optional;

public interface TokenRepositoryCustom {
    Optional<Token> findByAccessToken(String accessToken);

}

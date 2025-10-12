package kakaotech.community.domain.user;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LocalUserRepository implements UserRepository {
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDatabase.values().stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst();
    }
}

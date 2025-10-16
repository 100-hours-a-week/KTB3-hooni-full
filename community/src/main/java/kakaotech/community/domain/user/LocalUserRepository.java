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
    public synchronized Long save(User user) {
        if (user.getId() != null) {
            userDatabase.put(user.getId(), user);
            return user.getId();
        }

        user.assignId(idGenerator.getAndIncrement());
        userDatabase.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDatabase.values().stream()
                .filter(user -> user.isSameEmail(email))
                .findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDatabase.values().stream()
                .anyMatch(user -> user.isSameEmail(email));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userDatabase.values().stream()
                .anyMatch(user -> user.isSameNickname(nickname));
    }

    @Override
    public synchronized void delete(User user) {
        userDatabase.remove(user.getId());
    }

    public void clear() {
        userDatabase.clear();
        idGenerator.set(1);
    }
}

package dev.sarti.CrudFunctionalPattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class FunctionalPatternExampleMain {

    // ---- DOMAIN ----
    static class User {
        Long id;
        String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }

    // ---- FUNCTIONAL RESULT ----
    static class Result<T> {
        private final T value;
        private final Throwable error;

        private Result(T value, Throwable error) {
            this.value = value;
            this.error = error;
        }

        public static <T> Result<T> success(T value) {
            return new Result<>(value, null);
        }

        public static <T> Result<T> failure(Throwable error) {
            return new Result<>(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }

        public T getValue() {
            return value;
        }

        public Throwable getError() {
            return error;
        }
    }

    // ---- FAKE REPOSITORY ----
    static interface UserRepository {
        User save(User user) throws Exception;

        Optional<User> findById(Long id);

        void deleteById(Long id) throws Exception;
    }

    static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> store = new HashMap<>();
        private final AtomicLong sequence = new AtomicLong(1);

        @Override
        public User save(User user) throws Exception {
            if (user.getName() == null || user.getName().isEmpty()) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            if (user.getId() == null) {
                user.setId(sequence.getAndIncrement());
            } else if (!store.containsKey(user.getId())) {
                throw new NoSuchElementException("User with id " + user.getId() + " does not exist");
            }
            store.put(user.getId(), user);
            return user;
        }

        @Override
        public Optional<User> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public void deleteById(Long id) throws Exception {
            if (!store.containsKey(id)) {
                throw new NoSuchElementException("User with id " + id + " not found");
            }
            store.remove(id);
        }
    }

    // ---- FUNCTIONAL COMMANDS ----
    static class CreateUserCommand implements Function<User, Result<User>> {
        private final UserRepository repository;

        public CreateUserCommand(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public Result<User> apply(User user) {
            try {
                user.setId(null); // Ensure it's treated as new
                User saved = repository.save(user);
                return Result.success(saved);
            } catch (Exception e) {
                return Result.failure(e);
            }
        }
    }

    static class UpdateUserCommand implements Function<User, Result<User>> {
        private final UserRepository repository;

        public UpdateUserCommand(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public Result<User> apply(User user) {
            try {
                User updated = repository.save(user);
                return Result.success(updated);
            } catch (Exception e) {
                return Result.failure(e);
            }
        }
    }

    static class FindUserCommand implements Function<Long, Result<User>> {
        private final UserRepository repository;

        public FindUserCommand(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public Result<User> apply(Long id) {
            try {
                Optional<User> found = repository.findById(id);
                return found.map(Result::success)
                        .orElseGet(() -> Result.failure(new NoSuchElementException("User not found")));
            } catch (Exception e) {
                return Result.failure(e);
            }
        }
    }

    static class DeleteUserCommand implements Function<Long, Result<String>> {
        private final UserRepository repository;

        public DeleteUserCommand(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public Result<String> apply(Long id) {
            try {
                repository.deleteById(id);
                return Result.success("Deleted user with id: " + id);
            } catch (Exception e) {
                return Result.failure(e);
            }
        }
    }

    public static void main(String[] args) {
        UserRepository repository = new InMemoryUserRepository();
        CreateUserCommand createUser = new CreateUserCommand(repository);
        UpdateUserCommand updateUser = new UpdateUserCommand(repository);
        FindUserCommand findUser = new FindUserCommand(repository);
        DeleteUserCommand deleteUser = new DeleteUserCommand(repository);

        // Create users
        List<User> usersToCreate = Arrays.asList(
                new User(null, "Fabricio"),
                new User(null, "Sarti"));

        List<User> createdUsers = new ArrayList<>();
        for (User user : usersToCreate) {
            Result<User> result = createUser.apply(user);
            if (result.isSuccess()) {
                System.out.println("✅ Created: " + result.getValue());
                createdUsers.add(result.getValue());
            } else {
                System.out.println("❌ Create error: " + result.getError().getMessage());
            }
        }

        // Update user
        if (!createdUsers.isEmpty()) {
            User toUpdate = createdUsers.get(0);
            toUpdate.setName("Fabricio Sarti");
            Result<User> result = updateUser.apply(toUpdate);
            if (result.isSuccess()) {
                System.out.println("🔁 Updated: " + result.getValue());
            } else {
                System.out.println("❌ Update error: " + result.getError().getMessage());
            }
        }

        // Find user
        Result<User> findResult = findUser.apply(1L);
        if (findResult.isSuccess()) {
            System.out.println("🔍 Found: " + findResult.getValue());
        } else {
            System.out.println("❌ Find error: " + findResult.getError().getMessage());
        }

        // Delete user
        Result<String> deleteResult = deleteUser.apply(1L);
        if (deleteResult.isSuccess()) {
            System.out.println("🗑️ " + deleteResult.getValue());
        } else {
            System.out.println("❌ Delete error: " + deleteResult.getError().getMessage());
        }

        // Try to find deleted user
        Result<User> findAgain = findUser.apply(1L);
        System.out.println(findAgain.isSuccess()
                ? "Found again: " + findAgain.getValue()
                : "❌ Not found after delete: " + findAgain.getError().getMessage());
    }
}

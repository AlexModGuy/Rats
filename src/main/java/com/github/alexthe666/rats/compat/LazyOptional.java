package com.github.alexthe666.rats.compat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Compatibility shim for the removed LazyOptional class from NeoForge.
 * In 1.21, capabilities no longer use LazyOptional.
 */
public class LazyOptional<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean resolved = false;
    private boolean valid = true;

    private LazyOptional(@Nullable Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LazyOptional<T> of(@Nonnull Supplier<T> supplier) {
        return new LazyOptional<>(supplier);
    }

    public static <T> LazyOptional<T> empty() {
        LazyOptional<T> empty = new LazyOptional<>(null);
        empty.valid = false;
        return empty;
    }

    @Nonnull
    private T getValue() {
        if (!resolved) {
            resolved = true;
            if (supplier != null) {
                value = supplier.get();
            }
        }
        return value;
    }

    public boolean isPresent() {
        return valid && getValue() != null;
    }

    public void ifPresent(@Nonnull Consumer<? super T> consumer) {
        if (isPresent()) {
            consumer.accept(getValue());
        }
    }

    @Nonnull
    public Optional<T> resolve() {
        return isPresent() ? Optional.of(getValue()) : Optional.empty();
    }

    @Nullable
    public T orElse(@Nullable T other) {
        return isPresent() ? getValue() : other;
    }

    @SuppressWarnings("unchecked")
    public <X> LazyOptional<X> cast() {
        return (LazyOptional<X>) this;
    }

    public void invalidate() {
        this.valid = false;
    }

    @SuppressWarnings("unchecked")
    public <X> LazyOptional<X> lazyMap(java.util.function.Function<? super T, ? extends X> mapper) {
        return LazyOptional.of(() -> mapper.apply(getValue()));
    }

    /**
     * Maps the value if present, returning an Optional of the result.
     */
    public <U> Optional<U> map(java.util.function.Function<? super T, ? extends U> mapper) {
        if (isPresent()) {
            return Optional.ofNullable(mapper.apply(getValue()));
        }
        return Optional.empty();
    }

    public T orElseThrow() {
        if (!isPresent()) {
            throw new IllegalStateException("LazyOptional is empty");
        }
        return getValue();
    }
}





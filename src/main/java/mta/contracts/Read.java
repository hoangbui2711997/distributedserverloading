package mta.contracts;

/**
 * For client and admin
 */
public interface Read<T, R> {
    R read(T id);
}

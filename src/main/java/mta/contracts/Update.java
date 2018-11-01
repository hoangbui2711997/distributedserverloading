package mta.contracts;

/**
 * For client and admin
 */
public interface Update<T> {
    T update(T type);
}

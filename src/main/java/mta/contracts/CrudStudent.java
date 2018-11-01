package mta.contracts;

import java.io.Serializable;
import java.util.Collection;

/**
 * For admin only
 */
public interface CrudStudent<T> extends Serializable, ReadUpdateClient, CreateDeleteAdmin {
    Collection<T> readAll();
}

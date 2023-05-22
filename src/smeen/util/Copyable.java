package smeen.util;

/**
 * Represent class that can be copied
 * @param <T> the type to copy to
 */
public interface Copyable<T> {

    /**
     *
     * @return A copy version of this object, with identical necessary data.
     */
    T copy();

}

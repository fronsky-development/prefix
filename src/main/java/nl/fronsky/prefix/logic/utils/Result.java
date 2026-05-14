package nl.fronsky.prefix.logic.utils;

/**
 * A generic result wrapper that holds either a successful value or an exception.
 *
 * @param <T> the type of the result value
 */
public class Result<T> {
    private final T value;
    private final Exception exception;

    public Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    public T value() {
        return value;
    }

    public Exception exception() {
        return exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isExceptionType(Class<? extends Exception> exceptionClass) {
        return exceptionClass.isInstance(exception);
    }

    /**
     * Creates a successful Result.
     *
     * @param result the result value
     * @param <T>    the type of the result
     * @return a Result with the provided value and no error
     */
    public static <T> Result<T> ok(T result) {
        return new Result<>(result, null);
    }

    /**
     * Creates a failed Result.
     *
     * @param exception the exception that caused the failure
     * @param <T>       the type of the result
     * @return a Result with no value and the provided exception
     */
    public static <T> Result<T> fail(Exception exception) {
        return new Result<>(null, exception);
    }
}

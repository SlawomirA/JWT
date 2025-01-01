package thesis.backend.jwt.validator;

public interface Validator<T> {
    public boolean validate(T object);
    public String describe();
}

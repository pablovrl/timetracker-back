package cl.pablovillarroel.timetracker.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    private final String code;

    public ResourceAlreadyExistsException(String message) {
        this("RESOURCE_ALREADY_EXISTS", message);
    }

    public ResourceAlreadyExistsException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        this("RESOURCE_ALREADY_EXISTS", String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue));
    }

    public String getCode() {
        return code;
    }
}

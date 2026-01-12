package cl.pablovillarroel.timetracker.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final String code;

    public ResourceNotFoundException(String message) {
        this("RESOURCE_NOT_FOUND", message);
    }

    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        this("RESOURCE_NOT_FOUND", String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }

    public String getCode() {
        return code;
    }
}

package mta.contracts;

public enum REQUEST_TYPE {
    CREATE, DELETE, UPDATE, READ, SHUTDOWN, START;
    static final String SERVICES_PATH = "/services/";

    @Override
    public String toString() {
        return SERVICES_PATH + super.toString() + "/";
    }
}

package mta.clients;

import mta.contracts.ReadUpdateClient;
import mta.server.models.Student;

import java.util.UUID;

public class ClientServiceRequest implements ReadUpdateClient {
    @Override
    public Student read(UUID id) {
        return null;
    }

    @Override
    public Student update(Student type) {
        return null;
    }
}

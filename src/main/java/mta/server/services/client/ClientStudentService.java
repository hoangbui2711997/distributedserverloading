package mta.server.services.client;

import mta.contracts.ReadUpdateClient;
import mta.server.models.Student;
import mta.server.services.admin.AdminStudentService;

import java.util.UUID;

public class ClientStudentService implements ReadUpdateClient {
    private AdminStudentService adminStudentService = AdminStudentService.getInstance();

    @Override
    public Student read(UUID id) {
        return adminStudentService.read(id);
    }

    @Override
    public Student update(Student type) {
        return adminStudentService.update(type);
    }
}

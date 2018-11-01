package mta.admins;

import mta.configs.common.PackingDataRequest;
import mta.contracts.CrudStudent;
import mta.server.models.Student;

import java.util.Collection;
import java.util.UUID;

public class AdminServiceRequest implements CrudStudent<Student> {
    // request without body
    private PackingDataRequest packingDataRequest = new PackingDataRequest();
    // TODO: 28/10/2018 chua hoan thanh modun request from admin
    @Override
    public Student create(Student type) {
        return null;
    }

    @Override
    public Student delete(Student type) {
        return null;
    }

    @Override
    public Student read(UUID id) {
        return null;
    }

    @Override
    public Student update(Student type) {
        return null;
    }

    @Override
    public Collection<Student> readAll() {
        return null;
    }
}

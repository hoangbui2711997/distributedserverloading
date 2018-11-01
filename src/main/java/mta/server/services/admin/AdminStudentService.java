package mta.server.services.admin;

import com.google.common.collect.MapMaker;
import mta.contracts.CrudStudent;
import mta.server.models.Student;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class AdminStudentService implements CrudStudent<Student> {

    private static ConcurrentMap<UUID, Student> students;
    private static AdminStudentService adminStudentService;
    private static Object object = new Object();
    static {
        init();
    }
    public static AdminStudentService getInstance() {
        return adminStudentService;
    }
    private AdminStudentService() {

    }

    private static void init() {
        // init
        if(Objects.isNull(adminStudentService)) {
            synchronized (object) {
                if(Objects.isNull(adminStudentService)) {
                    adminStudentService = new AdminStudentService();
                }
                if(Objects.isNull(students)) {
                    students = new MapMaker()
                            .concurrencyLevel(2000)
                            .makeMap();

                    // TODO: 28/10/2018 read from file abc
                }

            }
        }

    }

    @Override
    public Student create(Student type) {
        assert students.containsKey(type.getId()) : "Student have been existed!";
        return students.putIfAbsent(type.getId(), type);
    }

    @Override
    public Student delete(Student type) {
        assert !students.containsKey(type.getId()) : "Student does not exist!";
        return students.remove(type.getId());
    }

    @Override
    public Student read(UUID id) {
        assert !students.containsKey(id) : "Student does not exist!";
        return students.get(id);
    }

    @Override
    public Student update(Student type) {
        assert !students.containsKey(type.getId()) : "Student does not exist!";
        return students.put(type.getId(), type);
    }

    @Override
    public Collection<Student> readAll() {
        return students.values();
    }
}

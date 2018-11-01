package mta.contracts;

import mta.server.models.Student;

import java.util.UUID;

public interface ReadUpdateClient extends Read<UUID, Student>, Update<Student>{
}

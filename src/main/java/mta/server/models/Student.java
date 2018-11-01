package mta.server.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static mta.server.services.times.TimeConvert.convertTimeZone;

@JacksonXmlRootElement
public class Student extends Distributed implements Serializable {
    private UUID id;
    private String name;
    private String title;
    private Integer age;
    private Login login;

    public Student(UUID id, String name, String title, Integer age) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.age = age;
        this.timeCreated = convertTimeZone(ZonedDateTime.now());
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public ZonedDateTime getTimeUpdate() {
        return this.timeUpdate;
    }

    public ZonedDateTime getTimeCreated() {
        return this.timeCreated;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(name, student.name) &&
                Objects.equals(title, student.title) &&
                Objects.equals(age, student.age) &&
                Objects.equals(login, student.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, age, login);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", age=" + age +
                ", login=" + login +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
        this.timeUpdate = convertTimeZone(ZonedDateTime.now());
    }
}

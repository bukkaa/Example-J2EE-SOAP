package ws.api;

import ws.api.validation.*;
import ws.api.validation.constraints.FooTypeConstraint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "time",
        "status",
        "bar"
})
@FooTypeConstraint
public class Foo {

    @FooTime
    @XmlElement(required = true)
    public String time;

    @FooStatus
    @XmlElement(required = true)
    public String status;

    @FooBar
    @XmlElement(required = true)
    public String bar;


    public Foo setTime(String value) {
        this.time = value;

        return this;
    }

    public Foo setStatus(String value) {
        this.status = value;

        return this;
    }

    public Foo setBar(String value) {
        this.bar = value;

        return this;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "time='" + time + '\'' +
                ", status='" + status + '\'' +
                ", bar='" + bar + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foo foo = (Foo) o;
        return Objects.equals(time, foo.time) &&
                Objects.equals(status, foo.status) &&
                Objects.equals(bar, foo.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, status, bar);
    }
}
}

package ws.api;

import ws.api.validation.*;
import ws.api.validation.constraints.TxTypeConstraint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

import path.to.utils.ToStr;
import path.to.utils.XsBase64;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "time",
        "status",
        "foo",
        "bar",
        "algorithm"
})
@TxTypeConstraint
public class TX {

    @TxTime
    @XmlElement(required = true)
    public String time;

    @TxStatusType
    @XmlElement(required = true)
    public String status;

    @XsBase64Type(name = "TX.foo")
    public XsBase64 foo;

    @TxBar
    @XmlElement(required = true)
    public String bar;


    public TX setTime(String value) {
        this.time = value;

        return this;
    }

    public TX setStatus(String value) {
        this.status = value;

        return this;
    }

    public TX setFoo(XsBase64 value) {
        this.foo = value;

        return this;
    }

    public TX setBar(String value) {
        this.bar = value;

        return this;
    }

    public TX setAlgorithm(String value) {
        this.algorithm = value;

        return this;
    }

    @Override
    public String toString() { // util class for tidy logging
        return ToStr.begin()
                .append("time", time)
                .append("status", status)
                .append("foo", foo)
                .append("bar", bar)
                .end();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TX tx = (TX) o;
        return Objects.equals(time, tx.time) &&
                Objects.equals(status, tx.status) &&
                Objects.equals(foo, tx.foo) &&
                Objects.equals(bar, tx.bar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, status, foo, bar);
    }
}
}

package geardesigner.beans;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * 记录一次计算
 *
 * @author SUPERSTATION
 */
public class Record implements Serializable {
    private static final long serialVersionUID = -2932634544905763020L;
    private final Specifications specs;
    private final LocalDateTime timestamp;

    public Record(@NotNull final Specifications specs, int year, int month, int day, long secondOfDay) {
        this.specs = specs;
        timestamp = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.ofSecondOfDay(secondOfDay));
    }

    public Record(@NotNull final Specifications specs) {
        this.specs = specs;
        timestamp = LocalDateTime.now();
    }

    public Specifications getSpecs() {
        return specs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Record{" +
                "specs=" + specs +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Record record = (Record) o;
        return specs.equals(record.specs) && timestamp.equals(record.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specs, timestamp);
    }
}

package fr.xebia.jqwik.exercise8.geo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
public final class Distance {

    public static final Distance ZERO = new Distance(0, false);
    public static final Distance ERROR = new Distance(-1, true);

    private final long meters;
    private final boolean error;

    public static Distance fromMeters(final long meters) {
        checkArgument(meters >= 0,
                "Distance must be positive (input value: %s meters)", meters);
        return new Distance(meters, false);
    }

    public static Distance fromKilometers(final double kilometers) {
        checkArgument(kilometers >= 0,
                "Distance must be positive (input value: %s km)", kilometers);
        final long meters = Double.valueOf(kilometers * 1_000).longValue();
        return new Distance(meters, false);
    }

    public long asMeters() {
        return this.meters;
    }

    public boolean isError() {
        return this.error;
    }

    public Distance minus(final Distance distance) {
        return this.isError() || distance.isError() || distance.isGreaterThan(this)
                ? ERROR
                : Distance.fromMeters(this.asMeters() - distance.asMeters());
    }

    public boolean isGreaterThan(final Distance distance) {
        return !this.isError()
                && !distance.isError()
                && this.asMeters() > distance.asMeters();
    }

    @Override
    public String toString() {
        return this.isError() ? "ERROR"
                : format("%s meters", this.asMeters());
    }
}

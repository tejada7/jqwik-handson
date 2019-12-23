package fr.xebia.jqwik.exercise8;

import com.google.common.collect.Range;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.toRadians;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@AllArgsConstructor(access = PRIVATE)
final class Latitude {

    private static final Range<Double> VALID_RANGE = Range.closed(0., 90.);

    private final double degrees;
    private final double radians;

    static Latitude fromDegrees(final double degrees) {
        checkArgument(VALID_RANGE.contains(degrees),
                "Latitude must be in %s° (input value: %s°)", VALID_RANGE, degrees);
        return new Latitude(degrees, toRadians(degrees));
    }

    double asDegrees() {
        return this.degrees;
    }

    double asRadians() {
        return this.radians;
    }

    @Override
    public String toString() {
        return format("%s°", this.asDegrees());
    }
}

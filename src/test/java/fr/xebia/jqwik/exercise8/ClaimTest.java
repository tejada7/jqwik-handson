package fr.xebia.jqwik.exercise8;

import fr.xebia.jqwik.exercise8.geo.Distance;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO #1: Replace Example-Based Testing with Property-Based Testing.
 * Consider how much of the solution you should accept duplicating in the test.
 */
class ClaimTest {

    @Property
    void should_resolve_overdistance_if_limit_is_undefined(@ForAll Contract contract, @ForAll IncidentType incidentType,
                                                           @ForAll Location incidentLocation, @ForAll Garage destination) {
        Assume.that(!contract.getLimits().containsKey(incidentType));

        final Claim claim = Claim.builder()
                .incident(new Incident(incidentType, incidentLocation))
                .contract(contract)
                .build();

        final Optional<Distance> actual = claim.resolveOverdistance(destination);

        assertThat(actual).contains(distanceBetween(incidentLocation, destination));
    }

    @Property
    void should_resolve_overdistance_if_garage_is_further_than_limit(@ForAll Distance limit, @ForAll IncidentType incidentType,
                                                                     @ForAll Location incidentLocation, @ForAll Garage destination) {
        Assume.that(distanceBetween(incidentLocation, destination).isGreaterThan(limit));

        final Claim claim = Claim.builder()
                .incident(new Incident(incidentType, incidentLocation))
                .contract(Contract.builder()
                        .limit(incidentType, limit)
                        .build())
                .build();

        final Optional<Distance> actual = claim.resolveOverdistance(destination);

        assertThat(actual).contains(distanceBetween(incidentLocation, destination).minus(limit));
    }

    @Property
    void should_resolve_no_overdistance_if_garage_is_closer_than_limit(@ForAll Distance limit, @ForAll IncidentType incidentType,
                                                                       @ForAll Location incidentLocation, @ForAll Garage destination) {
        Assume.that(limit.isGreaterThan(distanceBetween(incidentLocation, destination)));

        final Claim claim = Claim.builder()
                .incident(new Incident(incidentType, incidentLocation))
                .contract(Contract.builder()
                        .limit(incidentType, limit)
                        .build())
                .build();

        final Optional<Distance> actual = claim.resolveOverdistance(destination);

        assertThat(actual).isEmpty();
    }

    private static Distance distanceBetween(final Location origin, final Garage destination) {
        return origin.getCoordinates().distanceTo(destination.getCoordinates());
    }
}

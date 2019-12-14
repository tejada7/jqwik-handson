package fr.xebia.jqwik.exercise3;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

import java.util.Collection;

import static java.util.Arrays.asList;
import static net.jqwik.api.Arbitraries.strings;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

class AlertRoutingServiceTest {

    private static final Collection<String> COUNTRIES_WITH_SPECIFIC_SERVICES = asList("IT", "US");

    private final NotificationServiceForItaly notificationServiceForItaly = mock(NotificationServiceForItaly.class);
    private final NotificationServiceForUsa notificationServiceForUsa = mock(NotificationServiceForUsa.class);
    private final DefaultNotificationService defaultNotificationService = mock(DefaultNotificationService.class);
    private final AlertRoutingService routingService = new AlertRoutingService(notificationServiceForItaly, notificationServiceForUsa, defaultNotificationService);

    @Property
    void should_send_alert_to_specific_service_for_Italian_alert(@ForAll Alert.Type type) {
        final Alert alert = new Alert(type, "IT");

        routingService.send(alert);

        then(notificationServiceForItaly).should().notify(type.getCodeForItaly());
        then(notificationServiceForUsa).shouldHaveZeroInteractions();
        then(defaultNotificationService).shouldHaveZeroInteractions();
    }

    @Property
    void should_send_alert_code_to_specific_service_for_US_alert(@ForAll Alert.Type type) {
        final Alert alert = new Alert(type, "US");

        routingService.send(alert);

        then(notificationServiceForItaly).shouldHaveZeroInteractions();
        then(notificationServiceForUsa).should().notify(type.getCodeForUsa());
        then(defaultNotificationService).shouldHaveZeroInteractions();
    }

    /**
     * TODO: Add country as an argument of the test method, so that the test does not rely on an example.
     * <br/>
     * <p>Hint #1: <a href="https://jqwik.net/docs/current/user-guide.html#parameter-provider-methods">Parameter provider methods</a></p>
     * <p>Hint #2: <a href="https://jqwik.net/docs/current/user-guide.html#filtering">Filtering</a></p>
     * <p>Hint #3: Calling Mockito.reset(defaultNotificationService) is necessary, and it does not work from JUnit-lifecycle methods :(</p>
     */
    @Property
    void should_send_alert_message_to_default_service_for_alert_of_standard_country(@ForAll Alert.Type type, @ForAll("standardCountries") String country) {
        final Alert alert = new Alert(type, country);

        reset(defaultNotificationService);
        routingService.send(alert);

        then(notificationServiceForItaly).shouldHaveZeroInteractions();
        then(notificationServiceForUsa).shouldHaveZeroInteractions();
        then(defaultNotificationService).should().notify(type.getDefaultMessage());
    }

    @Provide
    Arbitrary<String> standardCountries() {
        return strings()
                .filter(code -> !COUNTRIES_WITH_SPECIFIC_SERVICES.contains(code));
    }

}

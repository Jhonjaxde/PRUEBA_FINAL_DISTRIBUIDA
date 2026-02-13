package uce.ec.todo.consul;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@ApplicationScoped
public class ConsulRegistration {

    private static final Logger LOG = Logger.getLogger(ConsulRegistration.class);

    @ConfigProperty(name = "consul.host", defaultValue = "localhost")
    String consulHost;

    @ConfigProperty(name = "consul.port", defaultValue = "8500")
    String consulPortStr;

    @ConfigProperty(name = "quarkus.http.port", defaultValue = "8082")
    String servicePortStr;

    private int getConsulPort() {
        try {
            return Integer.parseInt(consulPortStr);
        } catch (NumberFormatException e) {
            return 8500;
        }
    }

    private int getServicePort() {
        try {
            return Integer.parseInt(servicePortStr);
        } catch (NumberFormatException e) {
            return 8082;
        }
    }

    @ConfigProperty(name = "service.name", defaultValue = "todo-service")
    String serviceName;

    @ConfigProperty(name = "service.host")
    Optional<String> serviceHost;

    @ConfigProperty(name = "consul.enabled", defaultValue = "false")
    boolean consulEnabled;

    private String serviceId;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    void onStart(@Observes StartupEvent ev) {
        if (!consulEnabled) {
            LOG.info("Consul registration is disabled");
            return;
        }

        String host = serviceHost.orElse(getHostAddress());
        int servicePort = getServicePort();
        int consulPort = getConsulPort();
        serviceId = serviceName + "-" + host + "-" + servicePort;

        String registerJson = String.format("""
            {
                "ID": "%s",
                "Name": "%s",
                "Address": "%s",
                "Port": %d,
                "Tags": ["quarkus", "todo", "traefik.enable=true", "traefik.http.routers.todo-service.rule=PathPrefix(`/api/todos`)"],
                "Check": {
                    "HTTP": "http://%s:%d/q/health/live",
                    "Interval": "10s",
                    "Timeout": "5s",
                    "DeregisterCriticalServiceAfter": "1m"
                }
            }
            """, serviceId, serviceName, host, servicePort, host, servicePort);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + consulHost + ":" + consulPort + "/v1/agent/service/register"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(registerJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LOG.infof("Service %s registered successfully in Consul", serviceId);
            } else {
                LOG.errorf("Failed to register service in Consul: %s", response.body());
            }
        } catch (Exception e) {
            LOG.errorf("Error registering service in Consul: %s", e.getMessage());
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (!consulEnabled || serviceId == null) {
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + consulHost + ":" + getConsulPort() + "/v1/agent/service/deregister/" + serviceId))
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LOG.infof("Service %s deregistered from Consul", serviceId);
            } else {
                LOG.errorf("Failed to deregister service from Consul: %s", response.body());
            }
        } catch (Exception e) {
            LOG.errorf("Error deregistering service from Consul: %s", e.getMessage());
        }
    }

    private String getHostAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }
}

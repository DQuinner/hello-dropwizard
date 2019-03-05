package ie.dq.hellodropwizard;

import ie.dq.hellodropwizard.health.TemplateHealthCheck;
import ie.dq.hellodropwizard.resource.HelloDropwizardResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HelloDropwizardApplication extends Application<HelloDropwizardConfiguration> {

    public static void main(String[] args) throws Exception {
        new HelloDropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloDropwizardConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(HelloDropwizardConfiguration configuration, Environment environment) {
        final HelloDropwizardResource resource = new HelloDropwizardResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

}

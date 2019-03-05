package ie.dq.hellodropwizard;

import ie.dq.hellodropwizard.dao.PersonDAO;
import ie.dq.hellodropwizard.domain.Person;
import ie.dq.hellodropwizard.health.TemplateHealthCheck;
import ie.dq.hellodropwizard.resource.HelloDropwizardResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HelloDropwizardApplication extends Application<HelloDropwizardConfiguration> {

    public static void main(String[] args) throws Exception {
        new HelloDropwizardApplication().run(args);
    }

    private final HibernateBundle<HelloDropwizardConfiguration> hibernate = new HibernateBundle<HelloDropwizardConfiguration>(Person.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(HelloDropwizardConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "hello-dropwizard";
    }

    @Override
    public void initialize(Bootstrap<HelloDropwizardConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(HelloDropwizardConfiguration configuration, Environment environment) {
        final PersonDAO personDAO = new PersonDAO(hibernate.getSessionFactory());
        final HelloDropwizardResource helloDropwizardResource = new HelloDropwizardResource(
                configuration.getTemplate(),
                configuration.getDefaultName(),
                personDAO
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());

        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(helloDropwizardResource);
    }

}

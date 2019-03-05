package ie.dq.hellodropwizard.resource;

import com.codahale.metrics.annotation.Timed;
import ie.dq.hellodropwizard.dao.PersonDAO;
import ie.dq.hellodropwizard.domain.Person;
import ie.dq.hellodropwizard.ie.dq.hellodropwizard.api.Saying;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-dropwizard")
@Produces(MediaType.APPLICATION_JSON)
public class HelloDropwizardResource {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final PersonDAO personDAO;

    public HelloDropwizardResource(String template, String defaultName, PersonDAO personDAO) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.personDAO = personDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        personDAO.create(new Person(value));
        return new Saying(counter.incrementAndGet(), value);
    }

    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    public Person findPerson(@PathParam("id") LongParam id) {
        return personDAO.findById(id.get());
    }

}

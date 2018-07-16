package wireadmin.bench;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class DummyEventHandler extends BaseService<EventHandler> implements EventHandler {

	public DummyEventHandler(final BundleContext context) {
		super(EventHandler.class, context, getProperties());
	}

	private static Hashtable<String, Object> getProperties() {
		final Hashtable<String, Object> props = new Hashtable<>();

		props.put(EventConstants.EVENT_TOPIC, "/foo/bar");

		return props;
	}

	@Override
	public void handleEvent(Event event) {
		// do nothing
	}

}

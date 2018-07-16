package wireadmin.bench;

import org.osgi.framework.BundleContext;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Wire;

public class DummyConsumer extends BaseService<Consumer> implements Consumer {

	public DummyConsumer(final BundleContext context) {
		super(Consumer.class, context);
	}

	@Override
	public void updated(Wire wire, Object value) {
		if (Activator.LOG_ENABLED) {
			System.out.println(getPid() + " updated..");
		}
	}

	@Override
	public void producersConnected(Wire[] wires) {
		// does nothing
	}

}

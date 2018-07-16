package wireadmin.bench;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireAdmin;

public class Activator implements BundleActivator {

	static final boolean LOG_ENABLED = isLogEnabled();

	private List<BaseService<?>> registeredServices;
	private List<Wire> wires;

	private ServiceReference<WireAdmin> wireAdminRef;
	private WireAdmin wireAdmin;

	@Override
	public void start(BundleContext context) throws Exception {

		System.out.println("WireadminTest: starting...");

		this.registeredServices = new ArrayList<>();
		this.wires = new ArrayList<>();

		this.wireAdminRef = context.getServiceReference(WireAdmin.class);

		if (wireAdminRef == null) {
			System.out.println("Failed to get reference to WireAdmin");
			return;
		}

		this.wireAdmin = context.getService(wireAdminRef);

		final DummyProducer producer = new DummyProducer(context, getEmitInterval());
		registeredServices.add(producer);

		for (int i = 0; i < getConsumerCount(); i++) {
			final DummyConsumer consumer = new DummyConsumer(context);
			wires.add(wireAdmin.createWire(producer.getPid(), consumer.getPid(), null));
			registeredServices.add(consumer);
		}

		for (int i = 0; i < getEventHandlerCount(); i++) {
			registeredServices.add(new DummyEventHandler(context));
		}

		System.out.println("WireadminTest: starting...done");

	}

	private long getEmitInterval() {
		try {
			return Long.parseLong(System.getProperty("wireadmin.test.emit.interval.ms"));
		} catch (Exception e) {
			return 16;
		}
	}

	private static int getConsumerCount() {
		try {
			return Integer.parseInt(System.getProperty("wireadmin.test.consumer.count"));
		} catch (Exception e) {
			return 10;
		}
	}

	private static int getEventHandlerCount() {
		try {
			return Integer.parseInt(System.getProperty("wireadmin.test.event.handler.count"));
		} catch (Exception e) {
			return 5;
		}
	}

	private static boolean isLogEnabled() {
		try {
			return Boolean.parseBoolean(System.getProperty("wireadmin.test.log.enabled"));
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {

		System.out.println("WireadminTest: stopping...");

		for (final BaseService<?> service : registeredServices) {
			service.unregister();
		}

		if (wireAdmin != null) {
			for (final Wire w : wires) {
				wireAdmin.deleteWire(w);
			}
			context.ungetService(wireAdminRef);
		}

		System.out.println("WireadminTest: stopping...done");
	}
}

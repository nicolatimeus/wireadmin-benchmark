package wireadmin.bench;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class BaseService<T> {

	private static final AtomicInteger nextId = new AtomicInteger();
	private final String servicePid;
	private final ServiceRegistration<T> registration;

	public BaseService(final Class<T> serviceIntf, final BundleContext context) {
		this(serviceIntf, context, new Hashtable<>());
	}

	@SuppressWarnings("unchecked")
	public BaseService(final Class<T> serviceIntf, final BundleContext context,
			final Dictionary<String, Object> props) {
		this.servicePid = getClass().getSimpleName() + nextId.getAndIncrement();

		props.put("service.pid", servicePid);

		this.registration = context.registerService(serviceIntf, (T) this, props);
	}

	public String getPid() {
		return servicePid;
	}

	public void unregister() {
		registration.unregister();
	}
}

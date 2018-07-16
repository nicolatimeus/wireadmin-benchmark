package wireadmin.bench;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleContext;
import org.osgi.service.wireadmin.BasicEnvelope;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;

public class DummyProducer extends BaseService<Producer> implements Producer {

	private volatile Wire[] connectedWires;

	private ScheduledFuture<?> task;

	private final ScheduledExecutorService executor;

	public DummyProducer(final BundleContext context, final long intervalMs) {
		super(Producer.class, context);

		this.executor = Executors.newSingleThreadScheduledExecutor();

		this.task = this.executor.scheduleAtFixedRate(() -> {
			final Wire[] wires = this.connectedWires;
			if (Activator.LOG_ENABLED) {
				System.out.println(getPid() + " emitting..");
			}
			if (wires == null) {
				return;
			}
			for (final Wire w : wires) {
				w.update(new BasicEnvelope(null, getPid(), "test"));
			}
		}, 0, intervalMs, TimeUnit.MILLISECONDS);
	}

	@Override
	public Object polled(Wire wire) {
		return null;
	}

	@Override
	public void consumersConnected(Wire[] wires) {
		this.connectedWires = wires;
	}

	@Override
	public void unregister() {
		task.cancel(false);
		executor.shutdown();
		super.unregister();
	}
}

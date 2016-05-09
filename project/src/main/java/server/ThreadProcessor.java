package server;

public class ThreadProcessor implements Runnable {

	public boolean run = true;

	@Override
	public void run() {

		while (run) {
			try {
				MessageProcessing.dumpIfNeeded(System.currentTimeMillis());
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}

	}

}

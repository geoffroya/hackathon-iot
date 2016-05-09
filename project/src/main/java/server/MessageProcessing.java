package server;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.text.StrBuilder;
import org.rapidoid.http.Req;

import server.model.Message;

public class MessageProcessing {

	static File file = new File("save");
	static FileOutputStream fos;
	static {
		try {
			fos = new FileOutputStream(file, true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	// TODO : utiliser une meilleure gestion de verrou
	static Lock lock = new ReentrantLock();

	static Lock lockProcess = new ReentrantLock();

	static byte[] respBody = new byte[0];

	static List<Message> currentList = new ArrayList<>(1000);

	static List<Message> processingList = new ArrayList<>(1000);

	static int currentSize = 0;

	static long currentTs = System.currentTimeMillis();

	// Pour 10 threads => 7
	static int maxBufferSize = 7;
	static int maxBufferTimeinMs = 10;

	static Map<Integer, List<Message>> map = new HashMap<>();

	static DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static Req processMessage(Req req) {

		Message msg = BuildMessageObject(req);

		// System.out.println("ADD MSG");

		lock.lock();
		currentList.add(msg);
		currentSize++;
		lock.unlock();

		// FIXME: pourquoi ne pas laisser cela uniquement au thread ?
		dumpIfNeeded(null);
		req.async();
		return req;
	}

	private static Message BuildMessageObject(Req req) {
		// String id = req.posted("id");
		long ts = Instant.from(f.parse((String) req.posted("timestamp"))).getEpochSecond();
		int st = extractInt(req.posted("sensorType"));
		long val = extractLong(req.posted("value"));

		// System.out.println("VALUE:" + id + ";" + ts + ";" + st + ";" + val);

		Message msg = new Message(ts, st, val, req);
		return msg;
	}

	private static long extractLong(Object valObj) {
		long val = 0L;
		if (valObj instanceof String) {
			val = Long.parseLong((String) valObj);
		} else if (valObj instanceof Long) {
			val = (Long) valObj;
		} else if (valObj instanceof Integer) {
			val = (Integer) valObj;
		}
		return val;
	}

	private static int extractInt(Object valObj) {
		int val = 0;
		if (valObj instanceof String) {
			val = Integer.parseInt((String) valObj);
		} else if (valObj instanceof Long) {
			val = (Integer) valObj;
		} else if (valObj instanceof Integer) {
			val = (Integer) valObj;
		}
		return val;
	}

	public static void processListOfMessages(Long currentTsParam) {
		Long now = currentTsParam;
		if (now == null) {
			now = System.currentTimeMillis();
		}
		// System.out.println("DO JOB");
		lockProcess.lock();
		lock.lock();
		List<Message> tmp = processingList;
		processingList = currentList;
		currentList = tmp;
		currentSize = 0;

		currentTs = now;
		lock.unlock();

		if (processingList.size() > 0) {

			StrBuilder back = new StrBuilder();

			for (Message msg : processingList) {
				addMessageToRepository(msg);
				back.append(msg.sensorType).append(";").append(msg.timestamp).append(";").append(msg.value)
						.append("\n");
				// TODO : créer buffer
			}

			try {
				// Mettre en cache le FOS
				fos.write(back.toString().getBytes());
				fos.flush();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Dump buffer

			for (Message msg : processingList) {
				releaseSensorConnection(msg);
			}

			processingList.clear();

		}
		lockProcess.unlock();

	}

	private static void releaseSensorConnection(Message msg) {
		Req req = msg.req;
		req.response().body(respBody);
		req.done();
	}

	private static void addMessageToRepository(Message msg) {
		int st = msg.sensorType;
		List<Message> msgList = map.get(st);
		if (msgList == null) {
			msgList = new ArrayList<>();
			map.put(st, msgList);
		}
		msgList.add(msg);
	}

	public static Req reset(Req req) {

		lockProcess.lock();
		try {
			fos.close();
			file.delete();
			fos = new FileOutputStream(file);
			map = new HashMap<>();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			lockProcess.unlock();
		}

		System.gc();
		System.out.println("RESET");

		req.async().response().body(respBody);
		req.done();

		return req;
	}

	public static boolean dumpIfNeeded(Long now) {
		boolean res = ((now != null) && ((now - currentTs) > maxBufferTimeinMs));
		res = res || (currentSize >= maxBufferSize);
		if (res) {
			processListOfMessages(now);
		}
		return res;
	}
}

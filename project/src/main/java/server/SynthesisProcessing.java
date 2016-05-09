package server;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map.Entry;

import org.rapidoid.http.Req;

import server.model.Message;

public class SynthesisProcessing {

	static DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	static byte[] respBody = new byte[0];

	// TODO : mettre un str builder

	public static Req processSynthesis(Req req) {
		String dt = (String) req.param("timestamp");
		// dt = dt.replace(" ", "+");

		long start = Instant.from(f.parse(dt)).getEpochSecond();
		long duration = Integer.parseInt(req.param("duration"));
		long stop = start + duration;

		StringBuilder res = new StringBuilder();
		res.append("[");

		if (duration > 0) {
			boolean first = true;

			// TODO : ordonner la liste
			for (Entry<Integer, List<Message>> entry : MessageProcessing.map.entrySet()) {
				int st = entry.getKey();

				long nbValue = 0;
				long minValue = Long.MAX_VALUE;
				long maxValue = Long.MIN_VALUE;
				long sum = 0;

				for (Message msg : entry.getValue()) {
					long ts = msg.timestamp;
					long val = msg.value;
					if (ts >= start && ts <= stop) {
						nbValue++;
						minValue = Math.min(minValue, val);
						maxValue = Math.max(maxValue, val);
						sum += val;
					}
				}

				if (nbValue > 0) {
					long mean = (sum * 100) / nbValue;
					if (!first) {
						res.append(",");

					} else {
						first = false;
					}
					res.append("{\"sensorType\":").append(st);
					res.append(",\"minValue\":").append(minValue);
					res.append(",\"maxValue\":").append(maxValue);
					res.append(",\"mediumValue\":").append(mean);
					res.append("}");
				}

			}
		}
		res.append("]");

		// System.out.println("SYNTH:" + dt + "dur" + duration + "=> " +
		// res.toString() + "\n");

		req.async().response().body(res.toString().getBytes());
		req.done();
		// return res.toString();
		/*
		 * req.response().body(respBody); return null;
		 */

		return req;
	}
}

package server;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.rapidoid.http.Req;

import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrays;
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

		String goodRes = "";

		if (duration > 0) {
			String res1 = processSynthesisMethod1(start, stop);
			String res2 = processSynthesisMethod2(start, stop);
			// TODO : comparer res1 et res2
			goodRes = res2;

		}

		res.append(goodRes);
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

	private static String processSynthesisMethod2(long start, long stop) {
		StringBuilder res = new StringBuilder();
		Int2LongOpenHashMap minis = new Int2LongOpenHashMap();
		Int2LongOpenHashMap maxis = new Int2LongOpenHashMap();
		// Int2DoubleOpenHashMap averages = new Int2DoubleOpenHashMap();
		Int2DoubleOpenHashMap sums = new Int2DoubleOpenHashMap();
		TreeMap<Integer, Object> sensorMap = new TreeMap<Integer, Object>();
		int totalNbValues = 0;

		for (Entry<Long, SensorData> entry : SensorData.persistence.entrySet()) {
			if (entry.getKey() <= stop && (entry.getKey() + SensorData.deltaT > start)) {
				SensorData data = entry.getValue();

				for (Entry<Integer, SensorSynth> sensorSynth : data.synthesis.entrySet()) {
					int sensorType = sensorSynth.getKey().intValue();
					long min = sensorSynth.getValue().minValue;
					long max = sensorSynth.getValue().maxValue;
					// double medium = sensorSynth.getValue().mediumValue;
					long sum = sensorSynth.getValue().sumValue;
					int nbValues = sensorSynth.getValue().nbVal;

					sensorMap.put(sensorType, null);

					// mini
					if (!minis.containsKey(sensorType)) {
						minis.put(sensorType, min);
					} else {
						minis.put(sensorType, Math.min(min, minis.get(sensorType)));
					}

					// maxi
					if (!maxis.containsKey(sensorType)) {
						maxis.put(sensorType, max);
					} else {
						maxis.put(sensorType, Math.max(max, maxis.get(sensorType)));
					}

					// moy
					// if (!averages.containsKey(sensorType)) {
					// averages.put(sensorType, medium);
					// } else {
					// averages.put(sensorType, (totalNbValues *
					// averages.get(sensorType) + medium * nbValues)
					// / (totalNbValues + nbValues));
					// }
					if (!sums.containsKey(sensorType)) {
						sums.put(sensorType, sum);
					} else {
						sums.put(sensorType, sum + sums.get(sensorType));
					}
					totalNbValues += nbValues;
				}
			}
		}

		int[] sensors = minis.keySet().toIntArray();
		IntArrays.quickSort(sensors);
		boolean first = true;

		for (int st : sensors) {
			if (!first) {
				res.append(",");
			} else {
				first = false;
			}
			res.append("{\"sensorType\":").append(st);
			res.append(",\"minValue\":").append(minis.get(st));
			res.append(",\"maxValue\":").append(maxis.get(st));
//			res.append(",\"mediumValue\":").append((long) (averages.get(st) * 100.0d));
			res.append(",\"mediumValue\":").append((long) (sums.get(st) * 100.0d / totalNbValues));
			res.append("}");
		}

		return res.toString();
	}

	private static String processSynthesisMethod1(long start, long stop) {
		StringBuilder res = new StringBuilder();
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
		return res.toString();
	}
}

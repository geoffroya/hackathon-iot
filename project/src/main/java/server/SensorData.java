package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import server.model.Message;

public class SensorData {
	// attributs
	public Map<Integer, SensorSynth> synthesis;

	// Dirty => si les synthese ne sont pas à jour
	public boolean dirty = true;
	public boolean opened = false;
	public Path filePath = null;
	public BufferedWriter fileOutput;

	public static String saveDir = "/var/lib/iot/";
	public static long startTimestamp = Instant.now().getEpochSecond();

	// deltaT est la durée de stockage dans un fichier en secondes
	static int deltaT = 1;
	// Map pour stocker les données / dT. Key = timestamp // Value = {sensor,
	// min, max, medium, file, dirtyFlag, openFlag}
	public static Map<Long, SensorData> persistence = new Long2ObjectOpenHashMap<SensorData>();

	// FIXME : static method to get synthetis from all files
	// FIXME : appeler la persistence
	// FIXME : faire une alsse de +haut niveau pour gérer la persistence

	public SensorData(long ts) {
		this.dirty = true;
		try {
			this.synthesis = new Int2ObjectOpenHashMap<SensorSynth>();
			this.filePath = FileSystems.getDefault().getPath(saveDir, Long.toString(ts));
			this.fileOutput = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE);
			this.opened = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void append(Message msg) {
		// 1- trouver la bonne MAP / la créer si inexistante
		long msgTs = msg.timestamp;
		long ts = msgTs - ((msgTs - startTimestamp) % deltaT);
		SensorData data = persistence.get(ts);
		if (null == data) {
			data = new SensorData(ts);
			persistence.put(ts, data);
		}
		// 2- Insérer la donnée
		data.ingest(msg);
	}

	private void ingest(Message msg) {
		SensorSynth synth = synthesis.get(msg.sensorType);
		if (null == synth) {
			synth = new SensorSynth();
			synthesis.put(msg.sensorType, synth);
		}

		// max
		synth.maxValue = synth.maxValue > msg.value ? synth.maxValue : msg.value;
		// min
		synth.minValue = synth.minValue < msg.value ? synth.minValue : msg.value;
		// moyenne (en fait on conserve juste la somme)
		// synth.mediumValue = (synth.mediumValue * synth.nbVal + msg.value) /
		// (synth.nbVal + 1);
		synth.sumValue += msg.value;
		// nb val
		synth.nbVal++;

		// 2- Add data
		try {
			System.out.println("Writing to file...");
			fileOutput.append(Integer.toString(msg.sensorType)).append(";").append(Long.toString(msg.timestamp))
					.append(";").append(Long.toString(msg.value)).append("\n");
			// FIXME: flush depuis un thread dédié qui parcourt les SensorData
			// + ad flag flushed
			fileOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}

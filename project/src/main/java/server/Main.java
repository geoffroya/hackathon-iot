package server;

import java.util.HashSet;
import java.util.Set;

import org.rapidoid.config.Conf;
import org.rapidoid.http.FastHttp;
import org.rapidoid.setup.On;

import server.http.CustomBoonHttpParser;

// TODO Parsing de date a optimizer
public class Main {

	public static Set<Integer> sensorType = new HashSet<>();

	public static void main(String[] args) throws Throwable {
		// Initialize the configuration:
		Conf.args(args);

		On.post("/messages").plain(req -> MessageProcessing.processMessage(req));
		On.get("/messages/synthesis").json(req -> SynthesisProcessing.processSynthesis(req));
		On.get("/messages/reset").json(req -> MessageProcessing.reset(req));
		FastHttp.setHttpParser(new CustomBoonHttpParser());
		// FastHttp

		// TODO ajouter une commande pour reparametrer le serveur => seuil de
		// buffer ,...
		// TODO ajouter une commande pour avori l'etat du serveur

		Thread th = new Thread(new ThreadProcessor());
		th.start();
	}
}

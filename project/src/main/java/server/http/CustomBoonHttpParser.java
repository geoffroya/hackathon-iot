package server.http;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.rapidoid.buffer.Buf;
import org.rapidoid.data.BufRange;
import org.rapidoid.data.KeyValueRanges;
import org.rapidoid.http.impl.HttpParser;
import org.rapidoid.io.Upload;
import org.rapidoid.net.impl.RapidoidHelper;
import org.rapidoid.u.U;

import io.advantageous.boon.json.JsonParserFactory;

public class CustomBoonHttpParser extends HttpParser {

	JsonParserFactory factory = new JsonParserFactory();

	public CustomBoonHttpParser() {

		factory.setLazyChop(true);
		// jsonParserAndMapper = parser();

	}

	@Override
	/**
	 * @return <code>false</code> if JSON data was posted, so it wasn't
	 *         completely parsed.
	 */
	public boolean parseBody(Buf src, KeyValueRanges headers, BufRange body, KeyValueRanges data,
			Map<String, List<Upload>> files, RapidoidHelper helper) {

		if (body.isEmpty()) {
			return true;
		}

		return false;

	}

	@Override
	@SuppressWarnings("unchecked")
	public void parsePosted(Buf input, KeyValueRanges headersKV, BufRange rBody, KeyValueRanges posted,
			Map<String, List<Upload>> files, RapidoidHelper helper, Map<String, Object> dest) {

		boolean completed = parseBody(input, headersKV, rBody, posted, files, helper);

		Map<String, String> urlEncodedParamsDest = U.cast(dest);
		posted.toMap(input, true, true, urlEncodedParamsDest);

		if (!completed && !rBody.isEmpty()) {
			// Specific JSON parser
			String body = input.get(rBody);
			if (StringUtils.isNotBlank(body)) {
				String tmp = StringUtils.trim(body);
				tmp = body.substring(1, body.length() - 1);				
				String[] array = StringUtils.split(tmp, ',');
				if ((array != null)) {
					for (String pair : array) {
						String[] arrayPair = StringUtils.split(pair, ":",2);
						if (arrayPair != null) {
							String key = StringUtils.trim(arrayPair[0]);
							key = key.substring(1, key.length() - 1);
							String val = StringUtils.trim(arrayPair[1]);
							if(StringUtils.startsWith(val, "\"")){
								val = val.substring(1, val.length() - 1);
							}
							dest.put(key, val);
						}
					}
				}
			}
			/*
			 * Map<String, Object> jsonData = ( Map<String, Object> )
			 * factory.create().parse ( Map.class, body);
			 * 
			 * if (jsonData != null) { dest.putAll(jsonData); }
			 */
		}
	}

}

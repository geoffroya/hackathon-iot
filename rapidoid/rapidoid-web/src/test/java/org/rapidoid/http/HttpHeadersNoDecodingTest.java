package org.rapidoid.http;

/*
 * #%L
 * rapidoid-web
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.setup.On;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class HttpHeadersNoDecodingTest extends HttpTestCommons {

	@Test
	public void httpHeadersShouldNotBeDecoded() {
		On.get("/").plain(new ReqHandler() {
			@Override
			public Object execute(Req req) {
				return req.header("a+a-a") + ":::" + req.cookie("b+b-b");
			}
		});

		String a = "x-y+z++=123";
		String b = "c!@#d35f=-+1#";

		String resp = HTTP.get("http://localhost:8888/").header("a+a-a", a).cookie("b+b-b", b).fetch();

		eq(resp, a + ":::" + b);
	}

}

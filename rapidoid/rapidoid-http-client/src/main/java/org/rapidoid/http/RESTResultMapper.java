package org.rapidoid.http;

import org.rapidoid.RapidoidThing;
import org.rapidoid.data.Parse;
import org.rapidoid.lambda.Mapper;

/*
 * #%L
 * rapidoid-http-client
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

public class RESTResultMapper<T> extends RapidoidThing implements Mapper<byte[], T> {

	private final Class<T> resultType;

	public RESTResultMapper(Class<T> resultType) {
		this.resultType = resultType;
	}

	@Override
	public T map(byte[] src) throws Exception {
		return !resultType.equals(void.class) ? Parse.data(src, resultType) : null;
	}

}

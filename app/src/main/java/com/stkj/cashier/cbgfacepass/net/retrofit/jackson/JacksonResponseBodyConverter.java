/*
 * Copyright (C) 2015 Square, Inc.
 *
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
 */
package com.stkj.cashier.cbgfacepass.net.retrofit.jackson;

import com.fasterxml.jackson.databind.ObjectReader;
import com.stkj.cashier.cbgfacepass.net.callback.RetrofitConvertJsonListener;
import com.stkj.cashier.cbgfacepass.net.retrofit.RetrofitManager;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final ObjectReader adapter;

    JacksonResponseBodyConverter(ObjectReader adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            T obj = adapter.readValue(value.charStream());
            RetrofitConvertJsonListener convertJsonListener = RetrofitManager.INSTANCE.getConvertJsonListener();
            if (convertJsonListener != null) {
                convertJsonListener.onConvertJson(obj);
            }
            return obj;
        } finally {
            value.close();
        }
    }
}

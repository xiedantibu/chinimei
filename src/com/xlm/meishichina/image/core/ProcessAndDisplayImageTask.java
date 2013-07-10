/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xlm.meishichina.image.core;

import static com.xlm.meishichina.image.core.ImageLoader.LOG_POSTPROCESS_IMAGE;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.xlm.meishichina.image.core.process.BitmapProcessor;
import com.xlm.meishichina.image.utils.L;

/**
 * Presents process'n'display image task. Processes image {@linkplain Bitmap} and display it in {@link ImageView} using
 * {@link DisplayBitmapTask}.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
class ProcessAndDisplayImageTask implements Runnable {

	private final ImageLoaderEngine engine;
	private final Bitmap bitmap;
	private final ImageLoadingInfo imageLoadingInfo;
	private final Handler handler;

	public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.engine = engine;
		this.bitmap = bitmap;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;
	}

	@Override
	public void run() {
		if (engine.configuration.loggingEnabled) L.i(LOG_POSTPROCESS_IMAGE, imageLoadingInfo.memoryCacheKey);
		BitmapProcessor processor = imageLoadingInfo.options.getPostProcessor();
		final Bitmap processedBitmap = processor.process(bitmap);
		if (processedBitmap != bitmap) {
			bitmap.recycle();
		}
		handler.post(new DisplayBitmapTask(processedBitmap, imageLoadingInfo, engine));
	}
}

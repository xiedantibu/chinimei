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

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.xlm.meishichina.image.cache.disc.DiscCacheAware;
import com.xlm.meishichina.image.cache.disc.impl.FileCountLimitedDiscCache;
import com.xlm.meishichina.image.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.xlm.meishichina.image.cache.disc.impl.UnlimitedDiscCache;
import com.xlm.meishichina.image.cache.disc.naming.FileNameGenerator;
import com.xlm.meishichina.image.cache.disc.naming.HashCodeFileNameGenerator;
import com.xlm.meishichina.image.cache.memory.MemoryCacheAware;
import com.xlm.meishichina.image.cache.memory.impl.FuzzyKeyMemoryCache;
import com.xlm.meishichina.image.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.xlm.meishichina.image.core.assist.MemoryCacheUtil;
import com.xlm.meishichina.image.core.display.BitmapDisplayer;
import com.xlm.meishichina.image.core.display.SimpleBitmapDisplayer;
import com.xlm.meishichina.image.core.download.BaseImageDownloader;
import com.xlm.meishichina.image.core.download.ImageDownloader;
import com.xlm.meishichina.image.utils.StorageUtils;

/**
 * Factory for providing of default options for {@linkplain ImageLoaderConfiguration configuration}
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class DefaultConfigurationFactory {

	/** Create {@linkplain HashCodeFileNameGenerator default implementation} of FileNameGenerator */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/** Create default implementation of {@link DisckCacheAware} depends on incoming parameters */
	public static DiscCacheAware createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount) {
		if (discCacheSize > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new TotalSizeLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheSize);
		} else if (discCacheFileCount > 0) {
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);
			return new FileCountLimitedDiscCache(individualCacheDir, discCacheFileNameGenerator, discCacheFileCount);
		} else {
			File cacheDir = StorageUtils.getCacheDirectory(context);
			return new UnlimitedDiscCache(cacheDir, discCacheFileNameGenerator);
		}
	}

	/** Create default implementation of {@link MemoryCacheAware} depends on incoming parameters */
	public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize, boolean denyCacheImageMultipleSizesInMemory) {
		MemoryCacheAware<String, Bitmap> memoryCache = new UsingFreqLimitedMemoryCache(memoryCacheSize);
		if (denyCacheImageMultipleSizesInMemory) {
			memoryCache = new FuzzyKeyMemoryCache<String, Bitmap>(memoryCache, MemoryCacheUtil.createFuzzyKeyComparator());
		}
		return memoryCache;
	}

	/** Create default implementation of {@link ImageDownloader} - {@link BaseImageDownloader} */
	public static ImageDownloader createImageDownloader(Context context) {
		return new BaseImageDownloader(context);
	}

	/** Create default implementation of {@link BitmapDisplayer} */
	public static BitmapDisplayer createBitmapDisplayer() {
		return new SimpleBitmapDisplayer();
	}
}

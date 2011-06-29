/**
 *  Copyright 2003-2010 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sf.ehcache;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.MemoryUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Tests for Cache copy on r/w with pools
 *
 * @author Ludovic Orban
 */
public class CacheCopyOnRwPoolTest {

    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        cacheManager = new CacheManager(
                new Configuration()
                        .diskStore(new DiskStoreConfiguration().path(System.getProperty("java.io.tmpdir")))
                        .maxOnHeap(50, MemoryUnit.KILOBYTES)
                        .maxOnDisk(200, MemoryUnit.KILOBYTES)
        );
    }

    @After
    public void tearDown() {
        cacheManager.shutdown();
        cacheManager = null;
    }

    @Test
    public void testMemoryOnly() throws Exception {
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .statistics(true)
                        .name("memoryOnlyCache")
        ));
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .statistics(true)
                        .name("memoryOnlyCache_copy")
                        .copyOnRead(true)
                        .copyOnWrite(true)
        ));

        Cache cache = cacheManager.getCache("memoryOnlyCache");
        Cache copyCache = cacheManager.getCache("memoryOnlyCache_copy");

        cache.put(new Element(1000, "1000"));
        copyCache.put(new Element(1000, "1000"));

        long cacheSize = cache.calculateInMemorySize();
        System.out.println("cache size : " + cacheSize);
        assertTrue(cacheSize != 0);
        long copyCacheSize = copyCache.calculateInMemorySize();
        System.out.println("copyCache size : " + copyCacheSize);
        assertTrue(copyCacheSize != 0);
        assertTrue(cacheSize != copyCacheSize);
    }

    @Test
    public void testOverflowToDisk() throws Exception {
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .overflowToDisk(true)
                        .statistics(true)
                        .name("overflowToDiskCache")
        ));
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .overflowToDisk(true)
                        .statistics(true)
                        .name("overflowToDiskCache_copy")
                        .copyOnRead(true)
                        .copyOnWrite(true)
        ));

        Cache cache = cacheManager.getCache("overflowToDiskCache");
        Cache copyCache = cacheManager.getCache("overflowToDiskCache_copy");

        cache.put(new Element(1000, "1000"));
        copyCache.put(new Element(1000, "1000"));

        Thread.sleep(1000);

        long cacheSize = cache.calculateInMemorySize();
        System.out.println("cache size : " + cacheSize);
        assertTrue(cacheSize != 0);
        long copyCacheSize = copyCache.calculateInMemorySize();
        System.out.println("copyCache size : " + copyCacheSize);
        assertTrue(copyCacheSize != 0);
        assertTrue(cacheSize != copyCacheSize);

        long cacheDiskSize = cache.calculateOnDiskSize();
        System.out.println("cache disk size : " + cacheDiskSize);
        assertTrue(cacheDiskSize != 0);
        long copyCacheDiskSize = copyCache.calculateOnDiskSize();
        System.out.println("copyCache disk size : " + copyCacheDiskSize);
        assertTrue(copyCacheDiskSize != 0);
        assertTrue(cacheDiskSize != copyCacheDiskSize);
    }

    @Test
    public void testDiskPersistent() throws Exception {
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .overflowToDisk(true)
                        .diskPersistent(true)
                        .statistics(true)
                        .name("diskPersistentCache")
        ));
        cacheManager.addCache(new Cache(
                new CacheConfiguration()
                        .overflowToDisk(true)
                        .diskPersistent(true)
                        .statistics(true)
                        .name("diskPersistentCache_copy")
                        .copyOnRead(true)
                        .copyOnWrite(true)
        ));

        Cache cache = cacheManager.getCache("diskPersistentCache");
        Cache copyCache = cacheManager.getCache("diskPersistentCache_copy");

        cache.put(new Element(1000, "1000"));
        copyCache.put(new Element(1000, "1000"));

        Thread.sleep(1000);

        long cacheSize = cache.calculateInMemorySize();
        System.out.println("cache size : " + cacheSize);
        assertTrue(cacheSize != 0);
        long copyCacheSize = copyCache.calculateInMemorySize();
        System.out.println("copyCache size : " + copyCacheSize);
        assertTrue(copyCacheSize != 0);
        assertTrue(cacheSize != copyCacheSize);

        long cacheDiskSize = cache.calculateOnDiskSize();
        System.out.println("cache disk size : " + cacheDiskSize);
        assertTrue(cacheDiskSize != 0);
        long copyCacheDiskSize = copyCache.calculateOnDiskSize();
        System.out.println("copyCache disk size : " + copyCacheDiskSize);
        assertTrue(copyCacheDiskSize != 0);
        assertTrue(cacheDiskSize != copyCacheDiskSize);
    }

}


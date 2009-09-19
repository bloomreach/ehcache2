/**
 *  Copyright 2003-2009 Terracotta, Inc.
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
package net.sf.ehcache.management.provider;

import net.sf.ehcache.CacheManager;

/**
 * Implementations of this interface will can initialize MBeanRegistration for
 * the passed CacheManager.
 * This is in addition to the {@link ManagementService} and has nothing to do
 * 
 * <p />
 * 
 * @author <a href="mailto:asanoujam@terracottatech.com">Abhishek Sanoujam</a>
 * @since 1.7
 */
public interface MBeanRegistrationProvider {

    /**
     * Name of system-property name that controls whether MBeans for a
     * cacheManager should be registered or not when a CacheManager is created
     * by default. Allowed values are "true" and "false"
     */
    public static final String REGISTER_MBEANS_BY_DEFAULT_PROP_NAME = "net.sf.ehcache.jmx.register-mbeans-by-default";

    /**
     * Initialize MBeanRegistration if necessary for the cacheManager
     * 
     * @param cacheManager
     * @throws MBeanRegistrationProviderException
     */
    public void initialize(CacheManager cacheManager)
            throws MBeanRegistrationProviderException;

}

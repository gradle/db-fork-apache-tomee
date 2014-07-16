/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.openejb.test.entity.cmp2;


import org.apache.openejb.test.entity.cmp.ComplexCmpBeanPk;
import org.apache.openejb.test.entity.cmp.ComplexCmpHome;
import org.apache.openejb.test.entity.cmp.ComplexCmpObject;

import javax.rmi.PortableRemoteObject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * [2] Should be run as the second test suite of the BasicCmpTestClients
 */
public class Complex2HomeIntfcTests extends ComplexCmp2TestClient {
    public Complex2HomeIntfcTests() {
        super("HomeIntfc.");
    }

    protected void setUp() throws Exception {
        super.setUp();
        final Object obj = initialContext.lookup("client/tests/entity/cmp2/ComplexCmpHome");
        ejbHome = (ComplexCmpHome) javax.rmi.PortableRemoteObject.narrow(obj, ComplexCmpHome.class);
    }

    //===============================
    // Test home interface methods
    //
    public void test01_create() throws Exception {
        ejbObject = ejbHome.createObject("First Bean");
        assertNotNull("The EJBObject is null", ejbObject);
    }

    public void test02_findByPrimaryKey() throws Exception {
        ejbPrimaryKey = ejbObject.getPrimaryKey();
        assertTrue("Expected (ejbPrimaryKey instanceof ComplexCmpBeanPk) but was instanceof " + ejbPrimaryKey.getClass().getName(), ejbPrimaryKey instanceof ComplexCmpBeanPk);
        ejbObject = ejbHome.findByPrimaryKey((ComplexCmpBeanPk) ejbPrimaryKey);
        assertNotNull("The EJBObject is null", ejbObject);
    }

    public void test03_findByLastName() throws Exception {
        final Set<ComplexCmpBeanPk> keys = new HashSet<ComplexCmpBeanPk>();
        try {
            ejbObject = ejbHome.createObject("David Blevins");
            ejbPrimaryKey = ejbObject.getPrimaryKey();
            assertTrue("Expected (ejbPrimaryKey instanceof ComplexCmpBeanPk) but was instanceof " + ejbPrimaryKey.getClass().getName(), ejbPrimaryKey instanceof ComplexCmpBeanPk);
            keys.add((ComplexCmpBeanPk) ejbObject.getPrimaryKey());

            ejbObject = ejbHome.createObject("Dennis Blevins");
            ejbPrimaryKey = ejbObject.getPrimaryKey();
            assertTrue("Expected (ejbPrimaryKey instanceof ComplexCmpBeanPk) but was instanceof " + ejbPrimaryKey.getClass().getName(), ejbPrimaryKey instanceof ComplexCmpBeanPk);
            keys.add((ComplexCmpBeanPk) ejbObject.getPrimaryKey());

            ejbObject = ejbHome.createObject("Claude Blevins");
            ejbPrimaryKey = ejbObject.getPrimaryKey();
            assertTrue("Expected (ejbPrimaryKey instanceof ComplexCmpBeanPk) but was instanceof " + ejbPrimaryKey.getClass().getName(), ejbPrimaryKey instanceof ComplexCmpBeanPk);
            keys.add((ComplexCmpBeanPk) ejbObject.getPrimaryKey());
        } catch (final Exception e) {
            fail("Received exception while preparing the test: " + e.getClass() + " : " + e.getMessage());
        }

        try {
            final Collection objects = ejbHome.findByLastName("Blevins");
            final Set<ComplexCmpBeanPk> foundKeys = new HashSet<ComplexCmpBeanPk>();
            assertNotNull("The Collection is null", objects);
            assertEquals("The Collection is not the right size.", keys.size(), objects.size());
            for (final Object object : objects) {
                ejbObject = (ComplexCmpObject) PortableRemoteObject.narrow(object, ComplexCmpObject.class);

                // This could be problematic, it assumes the order of the collection.
                final ComplexCmpBeanPk foundKey = (ComplexCmpBeanPk) ejbObject.getPrimaryKey();
                assertTrue("Extra ejb found " + ejbObject.getPrimaryKey(), keys.contains(foundKey));
                foundKeys.add(foundKey);
            }

            keys.removeAll(foundKeys);
            assertEquals("Some keys were not found", Collections.EMPTY_SET, keys);
        } catch (final Exception e) {
            e.printStackTrace();
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }

    public void test04_homeMethod() {
        try {
            final int expected = 8;
            final int actual = ejbHome.sum(5, 3);
            assertEquals("home method returned wrong result", expected, actual);
        } catch (final Exception e) {
            fail("Received Exception " + e.getClass() + " : " + e.getMessage());
        }
    }
    //
    // Test home interface methods
    //===============================

}

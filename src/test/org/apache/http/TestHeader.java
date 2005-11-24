/*
 * $HeadURL$
 * $Revision$
 * $Date$
 * 
 * ====================================================================
 *
 *  Copyright 1999-2004 The Apache Software Foundation
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
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http;

import org.apache.http.io.CharArrayBuffer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for {@link NameValuePair}.
 *
 * @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
 */
public class TestHeader extends TestCase {

    public TestHeader(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestHeader.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(TestHeader.class);
    }

    public void testBasicConstructor() {
        Header header = new Header("name", "value", true);
        assertEquals("name", header.getName()); 
        assertEquals("value", header.getValue()); 
        assertTrue(header.isAutogenerated()); 
    }
    
    public void testBasicConstructorValueWithBlanks() {
        Header header = new Header("name", "  value  ", true);
        assertEquals("name", header.getName()); 
        assertEquals("value", header.getValue()); 
        assertTrue(header.isAutogenerated()); 
    }
    
    public void testBasicConstructorNullValue() {
        Header header = new Header("name", null, true);
        assertEquals("name", header.getName()); 
        assertEquals("", header.getValue()); 
        assertTrue(header.isAutogenerated()); 
    }
    
    public void testInvalidName() {
        try {
            new Header(null, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException ex) {
            //expected
        }
    }

    public void testCharArrayConstructor() throws Exception {
        CharArrayBuffer buffer1 = new CharArrayBuffer(16);
        buffer1.append("name:value");
        Header header1 = new Header(buffer1); 
        assertEquals("name", header1.getName());
        assertEquals("value", header1.getValue());

        CharArrayBuffer buffer2 = new CharArrayBuffer(16);
        buffer2.append("    name   :    value       ");
        Header header2 = new Header(buffer2); 
        assertEquals("name", header2.getName());
        assertEquals("value", header2.getValue());
    }

    public void testNullCharArrayConstructor() throws Exception {
        try {
            new Header(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }
    
    public void testMalformedCharArrayConstructor() throws Exception {
        try {
            CharArrayBuffer buffer = new CharArrayBuffer(16);
            buffer.append("  whatever  ");
            new Header(buffer);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
            // expected
        }
        try {
            CharArrayBuffer buffer = new CharArrayBuffer(16);
            buffer.append(" : whatever  ");
            new Header(buffer);
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException ex) {
            // expected
        }
    }
        
    public void testToString() {
        Header header1 = new Header("name1", "value1");
        assertEquals("name1: value1", header1.toString());
    }
    
    public void testHeaderElements() {
        Header header = new Header("name", "element1 = value1, element2; param1 = value1, element3");
        HeaderElement[] elements = header.getElements(); 
        assertNotNull(elements); 
        assertEquals(3, elements.length); 
        assertEquals("element1", elements[0].getName()); 
        assertEquals("value1", elements[0].getValue()); 
        assertEquals("element2", elements[1].getName()); 
        assertEquals(null, elements[1].getValue()); 
        assertEquals("element3", elements[2].getName()); 
        assertEquals(null, elements[2].getValue()); 
        assertEquals(1, elements[1].getParameters().length); 
    }    
        
}


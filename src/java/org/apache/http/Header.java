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

/**
 * <p>An HTTP header.</p>
 *
 * @author <a href="mailto:remm@apache.org">Remy Maucherat</a>
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * @version $Revision$ $Date$
 */
public class Header {

    /**
     * Header name.
     */
    private final String name;
    
    /**
     * Buffer containing the header in a raw form.
     */
    private final CharArrayBuffer buffer;
    
    /**
     * Position of the value in the header.
     */
    private final int posValue;
    
    /**
     * Autogenerated header flag.
     */
    private final boolean isAutogenerated;
    
    /**
     * Constructor with name and value, and autogenerated flag
     *
     * @param name the header name
     * @param value the header value
     * @param isAutogenerated <tt>true</tt> if the header is autogenerated,
     */
    public Header(final String name, String value, boolean isAutogenerated) {
        super();
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.posValue = name.length() + 2;
        int len = name.length() + 2 + (value != null ? value.length() : 0);
        this.buffer = new CharArrayBuffer(len);
        this.buffer.append(name);
        this.buffer.append(": ");
        if (value != null) {
            this.buffer.append(value);
        }
        this.isAutogenerated = isAutogenerated;
    }

    /**
     * Constructor with name and value
     *
     * @param name the header name
     * @param value the header value
     *  <tt>false</tt> otherwise.
     * 
     * @since 3.0
     */
    public Header(final String name, final String value) {
        this(name, value, false);
    }

    /**
     * <p> Constructor with char array buffer </p> 
     * <p> Please note this constructor does not make a deep copy of the 
     * char array buffer. If the char array is modified outside the Header
     * object, it may leave the object in a inconsistent state</p> 
     *
     * @param name the header name
     * @param value the header value
     * @param isAutogenerated <tt>true</tt> if the header is autogenerated,
     */
    public Header(final CharArrayBuffer buffer) throws ProtocolException {
        super();
        if (buffer == null) {
            throw new IllegalArgumentException("Char array buffer may not be null");
        }
        int comma = buffer.indexOf(':');
        if (comma == -1) {
            throw new ProtocolException("Invalid header: " + buffer.toString());
        }
        this.posValue = comma + 1;
        String s = buffer.substringTrimmed(0, comma);
        if (s.equals("")) {
            throw new ProtocolException("Invalid header: " + buffer.toString());
        }
        this.name = s;
        this.buffer = buffer;
        this.isAutogenerated = false;
    }

    /**
     * Returns the header name.
     *
     * @return String name The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the header value.
     *
     * @return String value The current value.
     */
    public String getValue() {
        return this.buffer.substringTrimmed(this.posValue, this.buffer.length());
    }

    /**
     * Returns the value of the auto-generated header flag.
     * 
     * @return <tt>true</tt> if the header is autogenerated,
     *  <tt>false</tt> otherwise.
     * 
     * @since 3.0
     */
    public boolean isAutogenerated() {
        return this.isAutogenerated;
    }

    /**
     * Returns a {@link String} representation of the header.
     *
     * @return a string
     */
    public String toString() {
        return buffer.toString();
    }

    /**
     * Returns an array of {@link HeaderElement}s constructed from my value.
     *
     * @see HeaderElement#parseElements(String)
     * 
     * @return an array of header elements
     * 
     * @since 3.0
     */
    public HeaderElement[] getElements() {
        return HeaderElement.parseElements(
                this.buffer.internBuffer(), this.posValue, this.buffer.length());
    }
    
}

/**
 * Copyright 2009-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.carp.security;

import org.carp.exception.CarpException;

/**
 * database password decrypt interface
 * all class implments this interface, and function
 * @author zhou
 * @version 0.3
 */
public interface IPasswordDecryptor {
	
	/**
	 * decrypt
	 * @param encoderString
	 * @return
	 * @throws CarpException
	 */
	String decrypt(String ciphertext)throws CarpException;
}

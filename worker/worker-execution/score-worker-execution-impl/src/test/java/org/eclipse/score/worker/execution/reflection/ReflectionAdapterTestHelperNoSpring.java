/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.eclipse.score.worker.execution.reflection;

import java.util.Map;

/**
 * @author Avi Moradi
 * @since 16/07/2014
 * @version $Id$
 */
public class ReflectionAdapterTestHelperNoSpring {

	@SuppressWarnings({ "static-method", "unused" })
	public Map<String, ?> myMethod_4(int parameter_1, int parameter_2, Map<String, ?> executionContext) {
		return executionContext;
	}

}

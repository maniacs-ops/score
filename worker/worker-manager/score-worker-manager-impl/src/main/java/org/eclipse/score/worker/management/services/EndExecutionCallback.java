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
package org.eclipse.score.worker.management.services;

/**
 * Created with IntelliJ IDEA.
 * User: kravtsov
 * Date: 21/11/12
 * Time: 14:14
 */
//TODO: Add Javadoc
//TODO: move module to the api module
public interface EndExecutionCallback {
    void endExecution(Long executionId);
}

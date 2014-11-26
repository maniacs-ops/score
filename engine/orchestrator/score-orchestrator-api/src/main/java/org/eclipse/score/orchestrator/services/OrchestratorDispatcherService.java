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
package org.eclipse.score.orchestrator.services;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 12/1/13
 *
 * @author
 */
//TODO: Add Javadoc Meir
public interface OrchestratorDispatcherService {
	void dispatch(List<? extends Serializable> messages, String bulkNumber, String wrv, String workerUuid);
}

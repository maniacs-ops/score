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

import org.eclipse.score.api.TriggeringProperties;

/**
 * User: wahnonm
 * Date: 27/01/14
 * Time: 11:20
 */
interface ScoreTriggering {

    /**
     * Trigger a flow by score & return the execution ID
     *
     * @param triggeringProperties object holding all the properties needed for the trigger
     * @return the execution ID
     */
    Long trigger(TriggeringProperties triggeringProperties);

    /**
     * Trigger a flow by score & return the execution ID
     *
     * @param executionId the execution ID we want to assign to the triggered execution
     * @param triggeringProperties object holding all the properties needed for the trigger
     * @return the execution ID
     */
    Long trigger(Long executionId, TriggeringProperties triggeringProperties);
}

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
package org.eclipse.score.engine.versioning.repositories;

import org.eclipse.score.engine.versioning.entities.VersionCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * User: wahnonm
 * Date: 31/10/13
 * Time: 16:26
 */
public interface VersionRepository extends JpaRepository<VersionCounter, Long> {

    VersionCounter findByCounterName(String counterName);

    @Modifying
    @Query("update VersionCounter v set v.versionCount=v.versionCount+1 where v.counterName = :counterName")
    int incrementCounterByName(@Param("counterName") String counterName);
}

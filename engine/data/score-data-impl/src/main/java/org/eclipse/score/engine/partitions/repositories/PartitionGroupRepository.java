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
package org.eclipse.score.engine.partitions.repositories;

import org.eclipse.score.engine.partitions.entities.PartitionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import static javax.persistence.LockModeType.PESSIMISTIC_READ;

/**
 * Date: 4/23/12
 *
 * @author
 */
public interface PartitionGroupRepository extends JpaRepository<PartitionGroup,Long> {

//	@Lock(PESSIMISTIC_READ)
	PartitionGroup findByName(String name);

	@Modifying
	@Query("update PartitionGroup p set p.activePartition = p.activePartition where p.name= :name")
	int lock(@Param("name") String name);
}

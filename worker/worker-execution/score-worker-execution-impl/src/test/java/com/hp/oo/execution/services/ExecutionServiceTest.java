package com.hp.oo.execution.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.score.worker.execution.WorkerConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.oo.broker.entities.BranchContextHolder;
import com.hp.oo.broker.entities.RunningExecutionPlan;
import com.hp.oo.broker.services.RuntimeValueService;
import com.hp.oo.enginefacade.execution.ExecutionStatus;
import com.hp.oo.enginefacade.execution.ExecutionSummary;
import com.hp.oo.enginefacade.execution.PauseReason;
import com.hp.oo.execution.reflection.ReflectionAdapter;
import com.hp.oo.execution.services.dbsupport.WorkerDbSupportService;
import com.hp.oo.internal.sdk.execution.Execution;
import com.hp.oo.internal.sdk.execution.ExecutionConstants;
import com.hp.oo.orchestrator.services.CancelExecutionService;
import com.hp.oo.orchestrator.services.PauseResumeService;
import com.hp.score.api.ControlActionMetadata;
import com.hp.score.api.ExecutionPlan;
import com.hp.score.api.ExecutionStep;
import com.hp.score.events.EventBus;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: kravtsov
 * Date: 02/12/12
 * Time: 14:11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ExecutionServiceTest {

	private static final Long RUNNING_EXE_PLAN_ID = 333L;
	private static final Long EXECUTION_STEP_1_ID = 1L;
	private static final Long EXECUTION_STEP_2_ID = 2L;
	static final Long EXECUTION_ID_1 = 1111L;
	static final Long EXECUTION_ID_2 = 2222L;

	@Autowired
	private ExecutionServiceImpl executionService;

	@Autowired
	private RuntimeValueService runtimeValueService;

	@Autowired
	private WorkerDbSupportService workerDbSupportService;

	@Autowired
	private PauseResumeService pauseResumeService;

	@Autowired
	private WorkerConfigurationService workerConfigurationService;

	@Before
	public void init() {
		Mockito.reset(runtimeValueService, workerDbSupportService, pauseResumeService);
	}

	@Test
	public void handlePausedFlow_NotPausedExecutionTest() {
		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(111L);
		exe.getSystemContext().setBrunchId("branch_id");
		exe.getSystemContext().put(ExecutionConstants.FLOW_UUID, "flow_uuid");
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_EVENTS_STEP_MAPPED, new HashMap<String, List>());

		//since the resumeService mock will return null - there no such execution in pause state, expect to get false
		boolean result = executionService.handlePausedFlow(exe);

		Assert.assertFalse(result);
	}

	@Test
	public void handlePausedFlow_UserPausedTest() {
		final Long executionId = 111L;
		final String branch_id = null;

		Execution exe = getExecutionObjToPause(executionId, branch_id);

		ExecutionSummary execSummary = new ExecutionSummary();
		execSummary.setPauseReason(PauseReason.USER_PAUSED);
		execSummary.setStatus(ExecutionStatus.PENDING_PAUSE);
		when(workerConfigurationService.isExecutionPaused(executionId, branch_id)).thenReturn(true);
		when(pauseResumeService.readPausedExecution(executionId, branch_id)).thenReturn(execSummary);

		boolean result = executionService.handlePausedFlow(exe);

		Mockito.verify(pauseResumeService, VerificationModeFactory.times(1)).writeExecutionObject(executionId, branch_id, exe);
		Assert.assertTrue(result);
	}

	@Test
	// branch is running, and parent is paused by the user -> branch should be paused
	public void handlePausedFlow_UserPausedParentTest() {
		final Long executionId = 111L;
		final String branch_id = "branch_id";

		Execution exe = getExecutionObjToPause(executionId, branch_id);

		// branch is not paused
		ExecutionSummary branch = new ExecutionSummary();
		branch.setStatus(ExecutionStatus.RUNNING);
		when(workerConfigurationService.isExecutionPaused(executionId, branch_id)).thenReturn(false);

		// parent is paused
		ExecutionSummary parent = new ExecutionSummary();
		parent.setPauseReason(PauseReason.USER_PAUSED);
		parent.setStatus(ExecutionStatus.PENDING_PAUSE);
		when(workerConfigurationService.isExecutionPaused(executionId, null)).thenReturn(true);
		when(pauseResumeService.readPausedExecution(executionId, null)).thenReturn(parent);

		boolean result = executionService.handlePausedFlow(exe);

		Mockito.verify(pauseResumeService, VerificationModeFactory.times(1)).pauseExecution(executionId, branch_id, PauseReason.USER_PAUSED);
		Mockito.verify(pauseResumeService, VerificationModeFactory.times(1)).writeExecutionObject(executionId, branch_id, exe);
		Assert.assertTrue(result);
	}

	private Execution getExecutionObjToPause(Long executionId, String branch_id) {
		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(executionId);
		exe.getSystemContext().setBrunchId(branch_id);
		exe.getSystemContext().put(ExecutionConstants.FLOW_UUID, "flow_uuid");
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_EVENTS_STEP_MAPPED, new HashMap<String, List>());
		//for events
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_ID_CONTEXT, executionId);
		return exe;
	}

	@Test
	public void isExecutionTerminatingTest() {
		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(111L);
		exe.setPosition(null);

		boolean result = executionService.isExecutionTerminating(exe);
		Assert.assertTrue(result);

		exe.setPosition(-1L);
		result = executionService.isExecutionTerminating(exe);
		Assert.assertTrue(result);

		exe.setPosition(-2L);
		result = executionService.isExecutionTerminating(exe);
		Assert.assertTrue(result);

		exe.setPosition(100L);
		result = executionService.isExecutionTerminating(exe);
		Assert.assertFalse(result);
	}

	@Test
	public void handleBranchFailureTest() {
		Set<String> locks = new HashSet<>();
		locks.add("lock_1");

		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(111L);
		exe.getSystemContext().put(ExecutionConstants.SPLIT_ID, "split_id");
		exe.getSystemContext().setBrunchId("branch_id");
		exe.getSystemContext().put(ExecutionConstants.ACQUIRED_LOCKS, (Serializable) locks);
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_EVENTS_STEP_MAPPED, new HashMap<String, List>());
		executionService.handleBranchFailure(exe, new Exception("Test exception..."));

		Mockito.verify(workerDbSupportService, VerificationModeFactory.times(1)).createBranchContext(any(BranchContextHolder.class));

		Mockito.verify(runtimeValueService, VerificationModeFactory.times(1)).remove(ExecutionConstants.LOCK_PREFIX_IN_DB + "lock_1");

	}

	@Test
	public void clearBranchLocksTest() {
		Set<String> locks = new HashSet<>();
		locks.add("lock_1");
		locks.add("lock_2");
		locks.add("lock_3");

		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(111l);
		exe.getSystemContext().put(ExecutionConstants.ACQUIRED_LOCKS, (Serializable) locks);
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_EVENTS_STEP_MAPPED, new HashMap<String, List>());
		executionService.clearBranchLocks(exe);

		//3 times runtimeService
		Mockito.verify(runtimeValueService, VerificationModeFactory.times(1)).remove(ExecutionConstants.LOCK_PREFIX_IN_DB + "lock_1");
		Mockito.verify(runtimeValueService, VerificationModeFactory.times(1)).remove(ExecutionConstants.LOCK_PREFIX_IN_DB + "lock_2");
		Mockito.verify(runtimeValueService, VerificationModeFactory.times(1)).remove(ExecutionConstants.LOCK_PREFIX_IN_DB + "lock_3");

		Assert.assertNull(exe.getSystemContext().get(ExecutionConstants.ACQUIRED_LOCKS));
	}

	@Test
	public void handleCancelledFlowsTest() {
		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(EXECUTION_ID_1);

		boolean result = executionService.handleCancelledFlow(exe);

		Assert.assertEquals(exe.getPosition(), null);
		Assert.assertEquals(result, true);

		exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(EXECUTION_ID_2);

		result = executionService.handleCancelledFlow(exe);

		Assert.assertEquals(exe.getPosition(), null);
		Assert.assertEquals(result, true);

	}

	@Test
	public void loadStepTest() {
		//FromSystemContext
		ExecutionStep executionStep = new ExecutionStep(EXECUTION_STEP_1_ID);

		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(EXECUTION_ID_1);

		exe.getSystemContext().put(ExecutionConstants.CONTENT_EXECUTION_STEP, executionStep);

		ExecutionStep loadedStep = executionService.loadExecutionStep(exe);

		Assert.assertEquals(executionStep.getExecStepId(), loadedStep.getExecStepId());

		//From DB
		executionStep = new ExecutionStep(EXECUTION_STEP_2_ID);
		ExecutionPlan exePlan = new ExecutionPlan();
		exePlan.addStep(executionStep);
		RunningExecutionPlan plan = new RunningExecutionPlan();
		plan.setExecutionPlan(exePlan);

		when(workerDbSupportService.readExecutionPlanById(RUNNING_EXE_PLAN_ID)).thenReturn(plan);

		executionStep = new ExecutionStep(EXECUTION_STEP_2_ID);

		exe = new Execution(RUNNING_EXE_PLAN_ID, EXECUTION_STEP_2_ID, new ArrayList<String>());

		loadedStep = executionService.loadExecutionStep(exe);

		Assert.assertEquals(executionStep.getExecStepId(), loadedStep.getExecStepId());
	}

	@Test
	public void executeStepTest() {
		//Test no exception is thrown - all is caught inside
		ExecutionStep executionStep = new ExecutionStep(EXECUTION_STEP_1_ID);
		executionStep.setActionData(new HashMap<String, Serializable>());

		Execution exe = new Execution(0L, 0L, new ArrayList<String>());

		executionService.executeStep(exe, executionStep);

		Assert.assertEquals(0, exe.getPosition().longValue()); //position is still 0
		Assert.assertTrue(exe.getSystemContext().containsKey(ExecutionConstants.EXECUTION_STEP_ERROR_KEY)); //there is error in context
	}

	@Test
	public void executeNavigationTest() {
		//Test no exception is thrown - all is caught inside
		ExecutionStep executionStep = new ExecutionStep(EXECUTION_STEP_1_ID);
		executionStep.setNavigation(new ControlActionMetadata("class", "method"));
		executionStep.setNavigationData(new HashMap<String, Serializable>());

		Execution exe = new Execution(0L, 0L, new ArrayList<String>());

		executionService.navigate(exe, executionStep);

		Assert.assertEquals(null, exe.getPosition()); //position was changed to NULL due to exception
		Assert.assertTrue(exe.getSystemContext().containsKey(ExecutionConstants.EXECUTION_STEP_ERROR_KEY)); //there is error in context
	}

	@Test
	public void postExecutionSettingsTest() {
		Execution exe = new Execution(0L, 0L, new ArrayList<String>());
		exe.setExecutionId(1111111L);

		exe.getSystemContext().put(ExecutionConstants.ACTUALLY_OPERATION_GROUP, "Real_Group");
		exe.getSystemContext().put(ExecutionConstants.MUST_GO_TO_QUEUE, true);
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_EVENTS_STEP_MAPPED, new HashMap<String, List>());
		//for events
		exe.getSystemContext().put(ExecutionConstants.EXECUTION_ID_CONTEXT, "stam");

		executionService.postExecutionSettings(exe);

		Assert.assertEquals("Real_Group", exe.getGroupName());
		Assert.assertEquals(true, exe.isMustGoToQueue());
		Assert.assertEquals(false, exe.getSystemContext().get(ExecutionConstants.MUST_GO_TO_QUEUE));
	}

	@Configuration
	static class ConfigurationForTest {

		@Bean
		public EventBus getEventBus() {
			return mock(EventBus.class);
		}

		@Bean
		public ExecutionServiceImpl getExecutionService() {
			return new ExecutionServiceImpl();
		}

		@Bean
		public WorkerConfigurationService getWorkerConfigurationService() {
			WorkerConfigurationService serviceMock = mock(WorkerConfigurationService.class);
			when(serviceMock.isExecutionCancelled(EXECUTION_ID_1)).thenReturn(true);
			when(serviceMock.isExecutionCancelled(EXECUTION_ID_2)).thenReturn(true);
			return serviceMock;
		}

		@Bean
		public ReflectionAdapter getReflectionAdapter() {
			ReflectionAdapter adapter = mock(ReflectionAdapter.class);

			//noinspection unchecked
			when(adapter.executeControlAction(any(ControlActionMetadata.class), any(Map.class))).thenThrow(RuntimeException.class);

			return adapter;
		}

		@Bean
		public WorkerDbSupportService getWorkerDbSupportService() {
			return mock(WorkerDbSupportService.class);
		}

		@Bean
		public PauseResumeService getPauseResumeService() {
			return mock(PauseResumeService.class);
		}

		@Bean
		public CancelExecutionService getCancelExecutionService() {
			return mock(CancelExecutionService.class);
		}

		@Bean
		public RuntimeValueService runtimeValueService() {
			return mock(RuntimeValueService.class);
		}

		@Bean
		public WorkerRecoveryManager workerRecoveryManager() {
			return mock(WorkerRecoveryManager.class);
		}

	}
}


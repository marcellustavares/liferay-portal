/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.kernel.workflow;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Micha Kiener
 * @author Brian Wing Shun Chan
 */
public class WorkflowException extends PortalException {

	public WorkflowException() {
	}

	public WorkflowException(String msg) {
		super(msg);
	}

	public WorkflowException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public WorkflowException(Throwable cause) {
		super(cause);
	}

	public static class MultipleInitialState extends WorkflowException {

		public MultipleInitialState(String state1, String state2) {
			super(
				String.format(
					"Multiple initial states %s and %s", state1, state2));

			_state1 = state1;
			_state2 = state2;
		}

		public String getState1() {
			return _state1;
		}

		public String getState2() {
			return _state2;
		}

		private final String _state1;
		private final String _state2;

	}

	public static class MustNotSetIncomingTransition extends WorkflowException {

		public MustNotSetIncomingTransition(String node) {
			super(
				String.format("An incoming transition was found for %s", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private String _node;

	}

	public static class MustPairedForkAndJoinNodes extends WorkflowException {

		public MustPairedForkAndJoinNodes(String fork, String node) {
			super(
				String.format(
					"Fork %s and join %s are not paired", fork, node));

			_fork = fork;
			_node = node;
		}

		public String getFork() {
			return _fork;
		}

		public String getNode() {
			return _node;
		}

		private String _fork;
		private String _node;

	}

	public static class MustSetAssignments extends WorkflowException {

		public MustSetAssignments(String task) {
			super(String.format("No assignments for task %s", task));

			_task = task;
		}

		public String getTask() {
			return _task;
		}

		private final String _task;

	}

	public static class MustSetIncomingTransition extends WorkflowException {

		public MustSetIncomingTransition(String node) {
			super(String.format("No incoming transition found for %s", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private String _node;

	}

	public static class MustSetInitialState extends WorkflowException {

		public MustSetInitialState() {
			super("No initial state defined");
		}

	}

	public static class MustSetJoinNode extends WorkflowException {

		public MustSetJoinNode(String fork) {
			super(String.format("No matching join found for fork %s", fork));

			_fork = fork;
		}

		public String getFork() {
			return _fork;
		}

		private String _fork;

	}

	public static class MustSetOutgoingTransition extends WorkflowException {

		public MustSetOutgoingTransition(String node) {
			super(
				String.format(
					"Less than 2 outgoing transitions found for %s", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private String _node;

	}

	public static class MustSetSourceNode extends WorkflowException {

		public MustSetSourceNode(String node) {
			super(String.format("Unable to find source node for %s", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTargetNode extends WorkflowException {

		public MustSetTargetNode(String node) {
			super(String.format("Unable to find target node for %s", node));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTerminalState extends WorkflowException {

		public MustSetTerminalState() {
			super("No terminal states defined");
		}

	}

	public static class UnbalancedForkAndJoinNodes extends WorkflowException {

		public UnbalancedForkAndJoinNodes() {
			super("There are unbalanced fork and join nodes");
		}

		public UnbalancedForkAndJoinNodes(String fork, String join) {
			super(
				String.format(
					"There are errors between fork %s and join %s", fork,
					join));

			_fork = fork;
			_join = join;
		}

		public String getFork() {
			return _fork;
		}

		public String getJoin() {
			return _join;
		}

		private String _fork;
		private final String _join;

	}

}
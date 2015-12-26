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

package com.liferay.workflow.definition.link.web.util.comparator;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.workflow.definition.link.web.search.WorkflowDefinitionLinkSearchEntry;

/**
 * @author Leonardo Barros
 */
public class WorkflowDefinitionLinkSearchEntryWorkflowComparator
	implements java.util.Comparator<WorkflowDefinitionLinkSearchEntry> {

	public WorkflowDefinitionLinkSearchEntryWorkflowComparator(
		boolean ascending) {

		_ascending = ascending;
	}

	@Override
	public int compare(
		WorkflowDefinitionLinkSearchEntry wdlse1,
		WorkflowDefinitionLinkSearchEntry wdlse2) {

		String workflowDefinitionName1 = StringUtil.toLowerCase(
			wdlse1.getWorkflowDefinitionName());

		String workflowDefinitionName2 = StringUtil.toLowerCase(
			wdlse2.getWorkflowDefinitionName());

		int value = workflowDefinitionName1.compareTo(workflowDefinitionName2);

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	private final boolean _ascending;

}
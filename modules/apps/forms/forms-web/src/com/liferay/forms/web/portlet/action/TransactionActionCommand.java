package com.liferay.forms.web.portlet.action;

import java.util.concurrent.Callable;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseActionCommand;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionAttribute;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;

public abstract class TransactionActionCommand extends BaseActionCommand {

	@Override
	protected void doProcessCommand(
			final PortletRequest portletRequest,
			final PortletResponse portletResponse)
		throws Exception {

		try {
			Callable<Void> callable = new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					doTransactionCommand(portletRequest, portletResponse);

					return null;
				}
			};

			TransactionInvokerUtil.invoke(_transactionAttribute, callable);
		}
		catch (Throwable t) {
			if (t instanceof PortalException) {
				throw (PortalException)t;
			}

			throw new SystemException(t);
		}
	}

	protected abstract void doTransactionCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws Exception;

	private static TransactionAttribute _transactionAttribute;

	static {
		TransactionAttribute.Builder builder =
			new TransactionAttribute.Builder();

		builder.setPropagation(Propagation.REQUIRES_NEW);
		builder.setRollbackForClasses(Exception.class);

		_transactionAttribute = builder.build();
	}

}
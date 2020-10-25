package sdm.utils;

//import engine.chat.ChatManager;
import engine.Engine;
import engine.EngineImpl;
import engine.chat.ChatManager;
import engine.notification.NotificationManager;
import engine.users.UserManager;
import sdm.constants.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static sdm.constants.Constants.INT_PARAMETER_ERROR;

//import static chat.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ENGINE_ATTRIBUTE_NAME = "engine";
	public static final String NOTIFICATION_MANAGER_ATTRIBUTE_NAME = "notificationManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object engineLock = new Object();
	private static final Object notificationManagerLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static Engine getEngine(ServletContext servletContext) {
		synchronized (engineLock) {
			if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new EngineImpl());
			}
		}
		return (Engine) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
	}

	public static NotificationManager getNotificationManager(ServletContext servletContext) {
		synchronized (notificationManagerLock) {
			if (servletContext.getAttribute(NOTIFICATION_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(NOTIFICATION_MANAGER_ATTRIBUTE_NAME, new NotificationManager());
			}
		}
		return (NotificationManager) servletContext.getAttribute(NOTIFICATION_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

	public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}
}

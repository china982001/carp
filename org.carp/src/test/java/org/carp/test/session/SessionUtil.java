package org.carp.test.session;

import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.cfg.CarpConfig;
import org.carp.exception.CarpException;

public class SessionUtil {
	private static ThreadLocal<CarpSession> local = new ThreadLocal<CarpSession>();
	private static CarpConfig config = null;
	private static CarpSessionBuilder builder = null;
	private static SessionUtil _sessionUtil = new SessionUtil();
	
	private SessionUtil(){
		try {
			config = new CarpConfig();
			builder = config.getSessionBuilder();
		} catch (CarpException e) {
			e.printStackTrace();
		}
	}
	
	public static CarpSessionBuilder getBuilder(){
		return builder;
	}
	
	public static CarpSession getSession(){
		CarpSession s = local.get();
		if(s == null || !s.isOpen())
			try {
				s = builder.getSession();
			} catch (CarpException e) {e.printStackTrace();
			}
		local.set(s);
		return s;
	}
	
	public static void close(){
		CarpSession s = local.get();
		try {
			s.close();
		} catch (CarpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = null;
		local.remove();
	}
	
	public static CarpSession getAnotherSession(){
		try {
			return builder.getSession();
		} catch (CarpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

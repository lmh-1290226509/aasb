package com.ddadai.baseviewlibrary;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ActivityManager_ {
	
	public static boolean webHomeBack = false;

	@SuppressWarnings("rawtypes")
	private static Map<Class, Object> actMaps=new HashMap<Class,Object>();
	
	

	//是否存在存活的activity
	public static boolean isAlive(Class<?> actCalss){
		if(actMaps.get(actCalss)!=null){
			Object obj=actMaps.get(actCalss);
			if(obj instanceof Activity){
				if(((Activity) obj).isFinishing()){
					return false;
				}
			}else if(obj instanceof ArrayList){
//				((List<Activity>)object).add(activity);
			}
		}else{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static void addActivity(Activity activity) {
		try {
//			currentAct=activity;
			if(activity==null||activity.isFinishing()){
				return;
			}
			@SuppressWarnings("rawtypes")
			Class c=activity.getClass();
			if(actMaps.get(c)==null){
				actMaps.put(c, activity);
			}else{
				Object object = actMaps.get(c);
				if(object instanceof Activity){
					Activity act=(Activity) object;
					if(act.isFinishing()){
						actMaps.put(c, activity);
					}else{
						List<Activity> acts=new ArrayList<Activity>();
						acts.add(act);
						acts.add(activity);
						actMaps.put(c, acts);
					}
				}else if(object instanceof ArrayList){
					((List<Activity>)object).add(activity);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeActivity(Class<?> activityClass) {
		try {
			if(activityClass==null||actMaps.get(activityClass)==null){
				return;
			}
			Object obj = actMaps.get(activityClass);
			if(obj instanceof Activity){
				Activity act=(Activity) obj;
				actMaps.remove(act);
				if(!act.isFinishing()){
					act.finish();
				}
			}else if(obj instanceof List){
				@SuppressWarnings("unchecked")
				List<Activity> acts=(List<Activity>) obj;
				actMaps.remove(acts);
				if(!acts.isEmpty()){
					for (Activity act : acts) {
						if(!act.isFinishing()){
							act.finish();
						}
					}
					acts.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void removeActivityExcept(Class<?> ...activityClass) {
		try {
			if(activityClass==null){
				return;
			}

			@SuppressWarnings("rawtypes")
			Map<Class, Object> tempMap=new HashMap<Class, Object>();
			for (Class<?> key : activityClass) {
				if(actMaps.containsKey(key)){
					tempMap.put(key, actMaps.get(key));
					actMaps.remove(key);
				}
			}
			finishAllActivity();
			actMaps.putAll(tempMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 结束所有Activity
	 */
	public static void finishAllActivity() {
		try {
			for (Object obj : actMaps.values()) {
				
				if(obj instanceof Activity){
					Activity act=(Activity) obj;
					if(!act.isFinishing()){
						act.finish();
					}
				}else if( obj instanceof List){
					@SuppressWarnings("unchecked")
					List<Activity> acts=(List<Activity>) obj;
					if(!acts.isEmpty()){
						for (Activity act : acts) {
							if(!act.isFinishing()){
								act.finish();
							}
						}
					}
				}
			}
			actMaps.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static Activity getAct(Class<?> actClass){
		try {
			Object obj=actMaps.get(actClass);
			if(obj== null){
				return null;
			}
			if(obj instanceof Activity ){
				return (Activity) obj;
			}
			if(obj instanceof ArrayList){
				@SuppressWarnings("rawtypes")
				ArrayList list=(ArrayList) obj;
				if(list.isEmpty()){
					return null;
				}else{
					return (Activity) list.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void exitAppaction() {
		finishAllActivity();
	}
}

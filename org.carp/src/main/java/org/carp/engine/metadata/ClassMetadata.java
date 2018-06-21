package org.carp.engine.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassMetadata {
	private Class<?> clz;
	private String name;
	private Map<String,FieldInfo> fields = new HashMap<>();
	private Map<String,MethodInfo> methods = new HashMap<>();
	
	public ClassMetadata(Class<?> clz){
		this.clz = clz;
		this.name = this.clz.getName();
	}
	
	public void putField(String fieldname, Field field){
		this.fields.put(fieldname, new FieldInfo(field));
	}
	public void putMethod(String name, Method method){
		this.methods.put(name, new MethodInfo(method));
	}
	
	public FieldInfo getField(String name){
		return this.fields.get(name);
	}
	
	public MethodInfo getMethod(String name){
		return this.methods.get(name);
	}
	
	public Class<?> getClz() {
		return clz;
	}

	public String getName() {
		return name;
	}



	public class MethodInfo{
		private Class<?> clazz;
		private Method method;
		
		public MethodInfo(Method m){
			this.method = m;
			this.clazz = ((this.method.getParameterTypes().length==0)?null:this.method.getParameterTypes()[0]);
			this.clazz = ((this.clazz == null) ? this.method.getReturnType() : this.clazz);
		}
		public Class<?> getClazz() {
			return clazz;
		}
		public Method getMethod() {
			return method;
		}
		
		public Object getValue(Object entity) throws Exception{
			return this.method.invoke(entity);
		}
		
		public void setValue(Object entity, Object value)throws Exception{
			this.method.invoke(entity, value);
		}
	}
	
	public class FieldInfo{
		private Field field;
		private Class<?> clazz;
		public FieldInfo(Field f){
			this.field = f;
			this.clazz = this.field.getType();
		}
		public Field getField() {
			return field;
		}
		public Class<?> getClazz() {
			return clazz;
		}
		
		public Object getValue(Object entity) throws Exception{
			boolean flag = this.field.isAccessible();
			this.field.setAccessible(true);
			Object value = this.field.get(entity);
			this.field.setAccessible(flag);
			return value;
		}
		
		public void setValue(Object entity, Object value)throws Exception{
			boolean flag = this.field.isAccessible();
			this.field.setAccessible(true);
			this.field.set(entity, value);
			this.field.setAccessible(flag);
		}
	}
}

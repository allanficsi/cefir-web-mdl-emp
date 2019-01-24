package br.com.aptare.cefit.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.LazyInitializationException;

public class RetirarLazy<T>
{
	private T obj;

	public RetirarLazy(T obj)
	{
		this.obj = obj;
	}

	private ArrayList<Object> processedObjects = new ArrayList<Object>();

	@SuppressWarnings("unchecked")
	public T execute()
	{
		return (T) execute(this.obj);
	}

	@SuppressWarnings("rawtypes")
	private Object execute(Object object)
	{
		if (object != null)
		{
			if (!existObject(object))
			{
				processedObjects.add(object);
				if (object.getClass().getPackage().getName().indexOf("br.com.aptare") != -1)
				{
					Field[] fields = object.getClass().getDeclaredFields();
					for (Field field : fields)
					{
						try
						{
							if (!field.getName().equalsIgnoreCase("serialVersionUID")
									&& !field.getName().equalsIgnoreCase("_filter_signature")
									&& !field.getName().equalsIgnoreCase("_methods_"))
							{
								Object obj = PropertyUtils.getProperty(object, field.getName());
								
								if (obj != null)
								{
									if (obj.getClass().getName().indexOf("javassist") != -1
											|| obj.getClass().getName().contains("jvs"))
									{
										PropertyUtils.setProperty(object, field.getName(), null);
									} else
									{
										execute(PropertyUtils.getProperty(object, field.getName()));
									}
								}
							}

						} catch (LazyInitializationException e)
						{
							try
							{
								PropertyUtils.setProperty(object, field.getName(), null);
							} catch (Exception ex)
							{
								e.printStackTrace();
							}
						} catch (IllegalAccessException e)
						{
							e.printStackTrace();
						} catch (InvocationTargetException e)
						{
							e.printStackTrace();
						} catch (NoSuchMethodException e)
						{
							e.printStackTrace();
						}
					}

				}

				if (object instanceof Collection)
				{
					Collection collection = (Collection) object;
					for (Object objectCollection : collection)
					{
						execute(objectCollection);
					}
				}

				if (object instanceof Map<?, ?>)
				{
					Map map = (Map) obj;
					Set<?> s = map.entrySet();
					for (Object object2 : s)
					{
						execute(object2);
					}
				}

			}
		}

		return object;

	}

	private boolean existObject(Object obj)
	{
		for (Object object : processedObjects)
		{
			if (obj == object)
			{
				return true;
			}

		}

		return false;
	}
}

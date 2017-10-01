package net.cactusthorn.initializer.properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class InitPropertiesJSONBuilder extends InitPropertiesBuilder {

	private static final Gson GSON = new Gson();
	
	@SuppressWarnings("unchecked")
	public InitPropertiesJSONBuilder loadFromJSON(Path path) throws IOException {
		
		Map<String,?> json;
		try (BufferedReader buf = Files.newBufferedReader(path ) ) {
			 
			json = (Map<String,?>)GSON.fromJson(buf, Map.class);
		}
		
		bean("", json);
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void bean(String prefix, Map<String,?> bean) {
		
		for (Map.Entry<String,?> entry: bean.entrySet() ) {
			
			if (entry.getValue() instanceof Map ) {
				
				bean(prefix + entry.getKey() + '.', (Map<String,?>)entry.getValue() );
				continue;
			}
			
			
			String propertyValue = value(entry.getValue() );
			if (propertyValue == null ) {
				continue;
			}
			
			put(prefix + entry.getKey(), propertyValue);
		}
	}
	
	private String value(Object object) {
		
		if (object == null ) {
			return null;
		}
		
		String propertyValue = null;
		
		if (object instanceof List ) {
			
			List<?> array = (List<?>)object;
			propertyValue = listValue(array);
		} else if (object instanceof Double ) {
			
			propertyValue = object.toString();
			int index = propertyValue.indexOf(".0");
			if (index > 0 && propertyValue.length() - index == 2 ) {
				propertyValue = propertyValue.substring(0, index); 
			}
		} else {
			
			propertyValue = object.toString();
		}
		
		return propertyValue;
	}
	
	private String listValue(List<?> array) {
		
		String propertyValue = mapValue(array);
		if (propertyValue == null) {
		
			StringBuilder sb = new StringBuilder();
			int startIndex = 0;
			
			for (Object a : array ) {
				
				if (startIndex != 0) {
					sb.append(valuesSeparator);
					startIndex++;
				}
				
				String value = value(a);
				
				sb.append(value);
				escape(sb, startIndex);
				startIndex = sb.length();
			}
			
			propertyValue = sb.toString();
		}
		
		return propertyValue;
	}
	
	private String mapValue(List<?> array) {
		
		Object first = array.get(0);
		
		if (!(first instanceof Map ) ) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		int startIndex = 0;
		
		for (Object a : array ) {
			
			Map<?,?> map = (Map<?,?>)a;
			
			for (Map.Entry<?,?> e : map.entrySet() ) {
				if (e.getValue() == null ) {
					continue;
				} 
				
				if (startIndex != 0) {
					sb.append(valuesSeparator);
					startIndex++;
				}
					
				sb.append(e.getKey());
				escape(sb, startIndex);
				
				sb.append(pairSeparator);
				startIndex = sb.length();
				
				String value = value(e.getValue());
				sb.append(value );
				escape(sb, startIndex);
				startIndex = sb.length();
			}
		}
		
		return sb.toString();
	}

	private void escape(StringBuilder sb, int startIndex) {
		escape(sb, '\\', startIndex);
		escape(sb, pairSeparator, startIndex);
		escape(sb, valuesSeparator, startIndex);
	}
	
	private void escape(StringBuilder sb, char escapeIt, int startIndex) {
		
		for (int index = startIndex; index < sb.length(); index++) {
		    if (sb.charAt(index) == escapeIt) {
		        sb.insert(index, '\\');
		        index++;
		    }
		}
	}
}

package net.cactusthorn.initializer.properties;

import java.util.ArrayList;
import java.util.List;

import net.cactusthorn.initializer.types.StringsPair;

public class Splitter implements Cloneable {
	
	private char valuesSep = ',';
	private String valuesSepAsStr = ",";
	private String valuesSepAsEscStr = "\\,";
	
	private char pairSep = '=';
	private String pairSepAsStr = "=";
	private String pairSepAsEscStr = "\\=";
	
	private boolean trimMultiValues;
	
	Splitter(char valuesSeparator, char pairSeparator, boolean trimMultiValues) {
		this.valuesSep = valuesSeparator;
		this.valuesSepAsStr = String.valueOf(this.valuesSep );
		this.valuesSepAsEscStr = "\\" + this.valuesSep;
		
		this.pairSep = pairSeparator;
		this.pairSepAsStr = String.valueOf(this.pairSep );
		this.pairSepAsEscStr = "\\" + this.pairSep;
		
		this.trimMultiValues = trimMultiValues;
	}
	
	/*
	 *  Drop Escaping only for \\ \, and \=
	 */
	//TODO need something more clever: too many replaces
	private String deleteEscapingComma(String str ) {
		String result = str.replace("\\\\","\\").replace(valuesSepAsEscStr, valuesSepAsStr);
		return trimMultiValues ? result.trim() : result; 
	}
	private String deleteEscapingCommaEquals(String str ) {
		String result = str.replace("\\\\","\\").replace(valuesSepAsEscStr, valuesSepAsStr).replace(pairSepAsEscStr,pairSepAsStr);
		return trimMultiValues ? result.trim() : result; 
	}
	
	public List<String> split(String str) {
		
		List<String> arr = new ArrayList<>();
		
		int pos = 0;
		int bs_couner = 0;
		for (int i = 0 ; i < str.length(); i++ ) {
			
			if (str.charAt(i) == valuesSep && bs_couner % 2 == 0  ) {
				arr.add(deleteEscapingComma(str.substring(pos, i) ) );
				pos = i + 1;
				bs_couner = 0;
			} else {
				if (str.charAt(i) == '\\') { 
					bs_couner++;
				} else {
					bs_couner = 0;
				}
			}
		}
		arr.add(deleteEscapingComma(str.substring(pos ) ) );
	
	    return arr;
	}
	
	public List<StringsPair> splitPairs(String str) {
		
		List<StringsPair> arr = new ArrayList<>();
		
		String currentKey = null;
		String lastValue = null;
		
		int pos = 0;
		int bs_couner = 0;
		for (int i = 0 ; i < str.length(); i++ ) {
			if (str.charAt(i) == valuesSep && bs_couner % 2 == 0 ) {
				
				if (!"".equals(currentKey) ) {
					lastValue = deleteEscapingCommaEquals(str.substring(pos, i) );
					arr.add(StringsPair.of(currentKey,lastValue));
				}
				currentKey = null;
				
				pos = i + 1;
				bs_couner = 0;
			} else if (str.charAt(i) == pairSep && bs_couner % 2 == 0 ) {
				
				if (currentKey != null && !currentKey.isEmpty() && lastValue == null ) {
					arr.add(StringsPair.of(currentKey));
				}
				currentKey = deleteEscapingCommaEquals(str.substring(pos, i) );
				lastValue = null;
				
				pos = i + 1;
				bs_couner = 0;
			} else {
				if (str.charAt(i) == '\\') { 
					bs_couner++;
				} else {
					bs_couner = 0;
				}
			}
		}
		if (!"".equals(currentKey) && currentKey != null  ) {
			arr.add(StringsPair.of(currentKey, deleteEscapingCommaEquals(str.substring(pos ) ) ) );
		}
	    return arr;
	}
}

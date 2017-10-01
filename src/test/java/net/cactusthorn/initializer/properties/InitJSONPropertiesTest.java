package net.cactusthorn.initializer.properties;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.SortedMap;

import org.junit.Test;
import static org.junit.Assert.*;

import net.cactusthorn.initializer.annotations.InitBean;
import net.cactusthorn.initializer.annotations.InitProperty;
import net.cactusthorn.initializer.annotations.InitPropertyName;
import net.cactusthorn.initializer.Initializer;

public class InitJSONPropertiesTest {

	static class TestBean2 {
		@InitPropertyName("int") int[] intarr;
		@InitProperty String string;
	}
	
	static class TestBean {
		@InitProperty boolean b1;
		@InitProperty String b2;
		@InitBean("sub-bean") TestBean2 bean2;
	}

	@InitProperty String str;
	
	@InitProperty boolean[] boolarr;
	
	@InitProperty @InitPropertyName("int") int $int;
	
	@InitProperty ZonedDateTime date;
	
	@InitProperty List<String> strarr;
	
	@InitProperty int[] intarr;
	@InitProperty SortedMap<Integer,String> map;
	
	@InitBean("bean") TestBean testBean;
	
	@Test
	public void testUtilPropertiesLoad() throws URISyntaxException, IOException {
		
		Path path = Paths.get(getClass().getClassLoader().getResource("init-simple.json").toURI());
		
		InitPropertiesJSONBuilder builder = new InitPropertiesJSONBuilder().loadFromJSON(path);
		
		InitProperties ip = builder.build();
		
		new Initializer().initialize(ip, this);
		
		assertEquals("simple string", str);
		
		assertArrayEquals(new boolean[] {true, true, true, false, true, false}, boolarr);

		assertEquals(-123, $int);
		
		assertEquals("2017-09-17T11:16:50+01:00", date.toString());
		
		assertEquals(3, strarr.size());
		
		assertEquals(3, map.size());
		assertEquals("BB", map.get(20));
		
		assertEquals("text", testBean.b2);
		
		assertArrayEquals(new int[] {100,200,300,400}, testBean.bean2.intarr);
	}
	
}

package test3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestTest {
	private static Test1 lab = new Test1();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSelection() {
		
		lab.Recvice[lab.count][1]=10;
		

		lab.select[7][0]=105;
		lab.select[8][0]=105;
		lab.select[9][0]=10;
		lab.select[10][0]=130;

		assertEquals(0, lab.Recvice[lab.count][0],0);
		assertEquals(0, lab.Recvice[lab.count][1],0);
		assertEquals(0, lab.Recvice[lab.count][2],0);
	}

}

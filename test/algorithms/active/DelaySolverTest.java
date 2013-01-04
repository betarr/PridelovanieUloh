package algorithms.active;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DelaySolverTest {

	@Test
	public void testAdd() {
		DelaySolver ds = new DelaySolver();
		ds.add(5, 6, 7, 8);
		ds.add(8, 9, 10, 11);
		ds.add(3, 4, 5, 6);
		ds.add(7, 8, 9, 10);
		ds.add(1, 2, 3, 4);
		
		List<Integer> expectedRs = Arrays.asList(new Integer[]{1, 3, 5, 7, 8});
		List<Integer> expectedDeltas = Arrays.asList(new Integer[]{2, 4, 6, 8, 9});
		List<Integer> expectedDs = Arrays.asList(new Integer[]{3, 5, 7, 9, 10});
		List<Integer> expectedCosts = Arrays.asList(new Integer[]{4, 6, 8, 10, 11});
		
		List<Integer> actualRs = ds.getRs();
		List<Integer> actualDeltas = ds.getDeltas();
		List<Integer> actualDs = ds.getDs();
		List<Integer> actualCosts = ds.getCosts();
		
		for (int i = 0; i < expectedRs.size(); i++) {
			assertEquals(expectedRs.get(i), actualRs.get(i));
			assertEquals(expectedDeltas.get(i), actualDeltas.get(i));
			assertEquals(expectedDs.get(i), actualDs.get(i));
			assertEquals(expectedCosts.get(i), actualCosts.get(i));
		}
	}
	
	@Test
	public void testCalculateResult() {
		DelaySolver ds = new DelaySolver();
		ds.setCriticalPathCost(9);
		ds.add(3, 5, 6, 2);
		ds.add(4, 3, 9, 3);
		ds.add(5, 4, 9, 4);
		
		int expectedResult = 12;
		int actualResult = ds.calculateDelay();
		assertEquals(expectedResult, actualResult);
	}
}

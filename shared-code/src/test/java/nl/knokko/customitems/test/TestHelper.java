package nl.knokko.customitems.test;

import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import nl.knokko.customitems.bithelper.BitInput;
import nl.knokko.customitems.bithelper.BitOutput;
import nl.knokko.customitems.bithelper.ByteArrayBitInput;
import nl.knokko.customitems.bithelper.ByteArrayBitOutput;

public class TestHelper {

	public static void testSaveLoad(
			Consumer<BitOutput> saveFunction,
			Consumer<BitInput> loadFunction
	) {
		ByteArrayBitOutput output = new ByteArrayBitOutput();
		saveFunction.accept(output);
		
		long testConstant = 943478232767823L;
		output.addLong(testConstant);
		
		BitInput input = new ByteArrayBitInput(output.getBytes());
		loadFunction.accept(input);
		
		// Test that the same number of bytes was read as was written
		assertEquals(testConstant, input.readLong());
	}
}

import exceptions.InvalidExpressionException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class Tests {
    public StringDecompression.StringInflater inflater = new StringDecompression.StringInflater();

    @Test
    public void assertResult_1() {
        assertEquals("xyxyxyxyzz", inflater.inflate("4[xy]2[z]"));
    }

    @Test
    public void assertResult_2() {
        assertEquals("xyzxyzxxyxyxxyxyxxyxyxxyxyz", inflater.inflate("2[xyz]4[x2[xy]]z"));
    }

    @Test
    public void assertZeroInvalidExpressionException_1() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("0008[xyz]4[xy]z"));
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void assertZeroInvalidExpressionException_2() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("0[xyz]4[xy]z"));
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void assertFormatInvalidExpressionException() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("[xyz]4[xy]z"));
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void assertNumberInvalidExpressionException() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("2[xyz]4[xy]4"));
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void assertSyntaxInvalidExpressionException() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("2-[xyz]*4[xy]4>"));
        assertNotNull(thrown.getMessage());
    }

    @Test
    public void assertBracketsInvalidExpressionException() {
        Throwable thrown = assertThrows(InvalidExpressionException.class, () -> inflater.inflate("2[xyz[]4[xy]]"));
        assertNotNull(thrown.getMessage());
    }




}

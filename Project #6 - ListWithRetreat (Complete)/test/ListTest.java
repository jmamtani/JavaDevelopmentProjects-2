import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.list.List;

/**
 * JUnit test fixture for {@code List<String>}'s constructor and kernel methods.
 *
 * @author Danny Kan (kan.74@osu.edu)
 * @author Jatin Mamtani (mamtani.6@osu.edu)
 *
 */
public abstract class ListTest {

    /**
     * Invokes the appropriate {@code List} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new list
     * @ensures constructorTest = (<>, <>)
     */
    protected abstract List<String> constructorTest();

    /**
     * Invokes the appropriate {@code List} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new list
     * @ensures constructorRef = (<>, <>)
     */
    protected abstract List<String> constructorRef();

    /**
     * Constructs a {@code List<String>} with the entries in {@code args} and
     * length of the left string equal to {@code leftLength}.
     *
     * @param list
     *            the {@code List} to construct
     * @param leftLength
     *            the length of the left string in the constructed {@code List}
     * @param args
     *            the entries for the list
     * @updates list
     * @requires list = (<>, <>) and 0 <= leftLength <= args.length
     * @ensures <pre>
     * list = ([first leftLength entries in args], [remaining entries in args])
     * </pre>
     */
    private void createFromArgsHelper(List<String> list, int leftLength,
            String... args) {
        for (String s : args) {
            list.addRightFront(s);
            list.advance();
        }
        list.moveToStart();
        for (int i = 0; i < leftLength; i++) {
            list.advance();
        }
    }

    /**
     * Creates and returns a {@code List<String>} of the implementation under
     * test type with the given entries.
     *
     * @param leftLength
     *            the length of the left string in the constructed {@code List}
     * @param args
     *            the entries for the list
     * @return the constructed list
     * @requires 0 <= leftLength <= args.length
     * @ensures <pre>
     * createFromArgs =
     *   ([first leftLength entries in args], [remaining entries in args])
     * </pre>
     */
    protected final List<String> createFromArgsTest(int leftLength,
            String... args) {
        assert 0 <= leftLength : "Violation of: 0 <= leftLength";
        assert leftLength <= args.length : "Violation of: leftLength <= args.length";
        List<String> list = this.constructorTest();
        this.createFromArgsHelper(list, leftLength, args);
        return list;
    }

    /**
     * Creates and returns a {@code List<String>} of the reference
     * implementation type with the given entries.
     *
     * @param leftLength
     *            the length of the left string in the constructed {@code List}
     * @param args
     *            the entries for the list
     * @return the constructed list
     * @requires 0 <= leftLength <= args.length
     * @ensures <pre>
     * createFromArgs =
     *   ([first leftLength entries in args], [remaining entries in args])
     * </pre>
     */
    protected final List<String> createFromArgsRef(int leftLength,
            String... args) {
        assert 0 <= leftLength : "Violation of: 0 <= leftLength";
        assert leftLength <= args.length : "Violation of: leftLength <= args.length";
        List<String> list = this.constructorRef();
        this.createFromArgsHelper(list, leftLength, args);
        return list;
    }

    /*
     * Complete and Systematic Test Cases:
     */

    /**
     * Testing the constructor.
     */
    @Test
    public final void testConstructor() {
        List<String> lActual = this.constructorTest();
        List<String> lExpected = this.constructorRef();
        assertEquals(lExpected, lActual);
    }

    /*
     * Kernel Methods --->
     */

    /*
     * Testing .addRightFront() in this section:=
     */

    /**
     * Testing .addRightFront() here...
     */
    @Test
    public final void testAddRightFrontLeftEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0, "red");
        lActual.addRightFront("red");
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .addRightFront() here...
     */
    @Test
    public final void testAddRightFrontLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "red", "blue");
        List<String> lExpected = this.createFromArgsRef(0, "green", "red",
                "blue");
        lActual.addRightFront("green");
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .addRightFront() here...
     */
    @Test
    public final void testAddRightFrontLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "purple", "red");
        lActual.addRightFront("red");
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .addRightFront() here...
     */
    @Test
    public final void testAddRightFrontLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(2, "yellow", "orange",
                "green", "purple");
        lActual.addRightFront("green");
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .removeRightFront() in this section:=
     */

    /**
     * Testing .removeRightFront() here...
     */
    @Test
    public final void testRemoveRightFrontLeftEmptyRightOne() {
        List<String> lActual = this.createFromArgsTest(0, "red");
        List<String> lExpected = this.createFromArgsRef(0);
        assertEquals("red", lActual.removeRightFront());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .removeRightFront() here...
     */
    @Test
    public final void testRemoveRightFrontLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(0, "red", "blue");
        assertEquals("green", lActual.removeRightFront());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .removeRightFront() here...
     */
    @Test
    public final void testRemoveRightFrontLeftNonEmptyRightOne() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple", "red");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "purple");
        assertEquals("red", lActual.removeRightFront());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .removeRightFront() here...
     */
    @Test
    public final void testRemoveRightFrontLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(2, "yellow", "orange",
                "purple");
        assertEquals("green", lActual.removeRightFront());
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .advance() in this section:=
     */

    /**
     * Testing .advance() here...
     */
    @Test
    public final void testAdvanceLeftEmptyRightOne() {
        List<String> lActual = this.createFromArgsTest(0, "red");
        List<String> lExpected = this.createFromArgsRef(1, "red");
        lActual.advance();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .advance() here...
     */
    @Test
    public final void testAdvanceLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(1, "green", "red",
                "blue");
        lActual.advance();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .advance() here...
     */
    @Test
    public final void testAdvanceLeftNonEmptyRightOne() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple", "red");
        List<String> lExpected = this.createFromArgsRef(4, "yellow", "orange",
                "purple", "red");
        lActual.advance();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .advance() here...
     */
    @Test
    public final void testAdvanceLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "green", "purple");
        lActual.advance();
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .moveToStart() in this section:=
     */

    /**
     * Testing .moveToStart() here...
     */
    @Test
    public final void testMoveToStartLeftEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0);
        lActual.moveToStart();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToStart() here...
     */
    @Test
    public final void testMoveToStartLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(0, "green", "red",
                "blue");
        lActual.moveToStart();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToStart() here...
     */
    @Test
    public final void testMoveToStartLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(0, "yellow", "orange",
                "purple");
        lActual.moveToStart();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToStart() here...
     */
    @Test
    public final void testMoveToStartLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(0, "yellow", "orange",
                "green", "purple");
        lActual.moveToStart();
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .leftLength() in this section:=
     */

    /**
     * Testing .leftLength() here...
     */
    @Test
    public final void testLeftLengthLeftEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0);
        assertEquals(0, lActual.leftLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .leftLength() here...
     */
    @Test
    public final void testLeftLengthLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(0, "green", "red",
                "blue");
        assertEquals(0, lActual.leftLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .leftLength() here...
     */
    @Test
    public final void testLeftLengthLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "purple");
        assertEquals(3, lActual.leftLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .leftLength() here...
     */
    @Test
    public final void testLeftLengthLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(2, "yellow", "orange",
                "green", "purple");
        assertEquals(2, lActual.leftLength());
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .rightLength() in this section:=
     */

    /**
     * Testing .rightLength() here...
     */
    @Test
    public final void testRightLengthLeftEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0);
        assertEquals(0, lActual.rightLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .rightLength() here...
     */
    @Test
    public final void testRightLengthLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(0, "green", "red",
                "blue");
        assertEquals(3, lActual.rightLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .rightLength() here...
     */
    @Test
    public final void testRightLengthLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "purple");
        assertEquals(0, lActual.rightLength());
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .rightLength() here...
     */
    @Test
    public final void testRightLengthLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(2, "yellow", "orange",
                "green", "purple");
        assertEquals(2, lActual.rightLength());
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing the iterator in this section:=
     */

    /**
     * Testing the iterator here...
     */
    @Test
    public final void testIteratorEmpty() {
        List<String> list1 = this.createFromArgsTest(0);
        List<String> list2 = this.createFromArgsRef(0);
        List<String> list3 = this.createFromArgsRef(0);
        for (String s : list1) {
            list2.addRightFront(s);
        }
        assertEquals(list3, list1);
        assertEquals(list3, list2);
    }

    /**
     * Testing the iterator here...
     */
    @Test
    public final void testIteratorOnlyRight() {
        List<String> list1 = this.createFromArgsTest(0, "red", "blue");
        List<String> list2 = this.createFromArgsRef(0);
        List<String> list3 = this.createFromArgsRef(0, "red", "blue");
        List<String> list4 = this.createFromArgsRef(0, "blue", "red");
        for (String s : list1) {
            list2.addRightFront(s);
        }
        assertEquals(list3, list1);
        assertEquals(list4, list2);
    }

    /**
     * Testing the iterator here...
     */
    @Test
    public final void testIteratorOnlyLeft() {
        List<String> list1 = this.createFromArgsTest(3, "red", "green", "blue");
        List<String> list2 = this.createFromArgsRef(0);
        List<String> list3 = this.createFromArgsRef(3, "red", "green", "blue");
        List<String> list4 = this.createFromArgsRef(0, "blue", "green", "red");
        for (String s : list1) {
            list2.addRightFront(s);
        }
        assertEquals(list3, list1);
        assertEquals(list4, list2);
    }

    /**
     * Testing the iterator here...
     */
    @Test
    public final void testIteratorLeftAndRight() {
        List<String> list1 = this.createFromArgsTest(2, "purple", "red",
                "green", "blue", "yellow");
        List<String> list2 = this.createFromArgsRef(0);
        List<String> list3 = this.createFromArgsRef(2, "purple", "red", "green",
                "blue", "yellow");
        List<String> list4 = this.createFromArgsRef(0, "yellow", "blue",
                "green", "red", "purple");
        for (String s : list1) {
            list2.addRightFront(s);
        }
        assertEquals(list3, list1);
        assertEquals(list4, list2);
    }

    /*
     * Testing .moveToFinish() in this section:=
     */

    /**
     * Testing .moveToFinish() here...
     */
    @Test
    public final void testMoveToFinishLeftEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0);
        lActual.moveToFinish();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToFinish() here...
     */
    @Test
    public final void testMoveToFinishLeftEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(0, "green", "red",
                "blue");
        List<String> lExpected = this.createFromArgsRef(3, "green", "red",
                "blue");
        lActual.moveToFinish();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToFinish() here...
     */
    @Test
    public final void testMoveToFinishLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "yellow", "orange",
                "purple");
        List<String> lExpected = this.createFromArgsRef(3, "yellow", "orange",
                "purple");
        lActual.moveToFinish();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToFinish() here...
     */
    @Test
    public final void testMoveToFinishLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(2, "yellow", "orange",
                "green", "purple");
        List<String> lExpected = this.createFromArgsRef(4, "yellow", "orange",
                "green", "purple");
        lActual.moveToFinish();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .moveToFinish() here...
     */
    @Test
    public final void testMoveToFinishShowBug() {
        List<String> lActual = this.createFromArgsTest(0);
        List<String> lExpected = this.createFromArgsRef(0, "red");
        lActual.moveToFinish();
        lActual.addRightFront("red");
        assertEquals(lExpected, lActual);
    }

    /*
     * Testing .retreat() in this section:=
     */

    /**
     * Testing .retreat() ONCE (i.e., 1x) w/ the left containing one (1)
     * {@code String} entries and the right containing zero (0) {@code String}
     * entries.
     */
    @Test
    public final void testRetreatLeftOneRightEmptyOnce() {
        List<String> lActual = this.createFromArgsTest(1, "red");
        List<String> lExpected = this.createFromArgsRef(0, "red");
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .retreat() TWICE (i.e., 2x) w/ the left containing two (2)
     * {@code String} entries and the right containing zero (0) {@code String}
     * entries.
     */
    @Test
    public final void testRetreatLeftTwoRightEmptyTwice() {
        List<String> lActual = this.createFromArgsTest(2, "red", "green");
        List<String> lExpected = this.createFromArgsRef(0, "red", "green");
        lActual.retreat();
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .retreat() ONCE (i.e., 1x) w/ the left containing one (1)
     * {@code String} entries and the right containing non-empty {@code String}
     * entries.
     */
    @Test
    public final void testRetreatLeftOneRightNonEmptyOnce() {
        List<String> lActual = this.createFromArgsTest(1, "red", "green",
                "blue");
        List<String> lExpected = this.createFromArgsRef(0, "red", "green",
                "blue");
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .retreat() TWICE (i.e., 2x) w/ the left containing two (2)
     * {@code String} entries and the right containing non-empty {@code String}
     * entries.
     */
    @Test
    public final void testRetreatLeftTwoRightNonEmptyTwice() {
        List<String> lActual = this.createFromArgsTest(2, "red", "green",
                "blue", "white");
        List<String> lExpected = this.createFromArgsRef(0, "red", "green",
                "blue", "white");
        lActual.retreat();
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .retreat() w/ the left containing non-empty {@code String}
     * entries and the right containing zero (0) {@code String} entries.
     */
    @Test
    public final void testRetreatLeftNonEmptyRightEmpty() {
        List<String> lActual = this.createFromArgsTest(4, "red", "green",
                "blue", "white");
        List<String> lExpected = this.createFromArgsRef(3, "red", "green",
                "blue", "white");
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /**
     * Testing .retreat() w/ the left containing non-empty {@code String}
     * entries and the right containing non-empty {@code String} entries.
     */
    @Test
    public final void testRetreatLeftNonEmptyRightNonEmpty() {
        List<String> lActual = this.createFromArgsTest(3, "red", "green",
                "blue", "white");
        List<String> lExpected = this.createFromArgsRef(2, "red", "green",
                "blue", "white");
        lActual.retreat();
        assertEquals(lExpected, lActual);
    }

    /*
     * Integration Testing (NOT REQUIRED):
     */

}

/**
 * model to be stored in the stack
 * @author Ben Cullivan
 */
public class StackItem {

    private final int row;
    private final int column;
    private int num;
    private boolean added;

    public StackItem(int num, int row, int column) {
        this.row = row;
        this.column = column;
        this.num = num;
        added = false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getNum() {
        return num;
    }

    public boolean getAdded() {
        return added;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

}

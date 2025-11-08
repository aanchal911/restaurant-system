public class Table {
    private int tableId;
    private String tableName;
    private int seats;
    private boolean occupied;

    public Table(int tableId, String tableName, int seats, boolean occupied) {
        this.tableId = tableId;
        this.tableName = tableName;
        this.seats = seats;
        this.occupied = occupied;
    }

    public int getTableId() { return tableId; }
    public String getTableName() { return tableName; }
    public int getSeats() { return seats; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
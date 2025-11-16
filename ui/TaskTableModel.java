package ui;

import javax.swing.table.AbstractTableModel;
import model.TaskV5Filtered;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {
    private final String[] columns = {"Done", "Title", "Description", "Priority", "Category", "Deadline"};
    private List<TaskV5Filtered> tasks;

    public TaskTableModel(List<TaskV5Filtered> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() { return tasks.size(); }
    @Override
    public int getColumnCount() { return columns.length; }
    @Override
    public String getColumnName(int column) { return columns[column]; }
    @Override
    public Class<?> getColumnClass(int columnIndex) { return columnIndex == 0 ? Boolean.class : String.class; }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex == 0; }

    @Override
    public Object getValueAt(int row, int col) {
        TaskV5Filtered t = tasks.get(row);
        return switch (col) {
            case 0 -> t.isCompleted();
            case 1 -> t.getTitle();
            case 2 -> t.getDescription();
            case 3 -> t.getPriority();
            case 4 -> t.getCategory();
            case 5 -> t.getDeadlineFormatted();
            default -> null;
        };
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        if (col == 0) {
            tasks.get(row).setCompleted((Boolean) aValue);
            fireTableCellUpdated(row, col);
        }
    }

    public TaskV5Filtered getTaskAt(int row) { return tasks.get(row); }
    public void setTasks(List<TaskV5Filtered> tasks) { this.tasks = tasks; fireTableDataChanged(); }
}

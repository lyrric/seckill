package com.github.lyrric.model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created on 2020-07-24.
 *
 * @author wangxiaodong
 */
public class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

package com.github.lyrric.ui;


import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.Member;
import com.github.lyrric.model.TableModel;
import com.github.lyrric.service.HttpService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created on 2020-07-24.
 * 选择接种人员页面
 * @author wangxiaodong
 */
public class MemberDialog extends JDialog {

    JTable table;
    DefaultTableModel model;

    JButton submitBtn;

    private boolean success = false;

    public MemberDialog(Frame owner) {
        super(owner, true);
        init();
    }

    public void init() {
        this.setLayout(null);
        String[] title = {"id", "姓名", "身份证"};
        model = new TableModel(new String[0][], title);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10,10,400,100);
        this.add(scrollPane);

        setTitle("加载成员中......");
        submitBtn = new JButton("确定");
        submitBtn.setBounds(165, 120, 100, 40);
        submitBtn.addActionListener(e->{
            submit();
        });
        this.add(submitBtn);

        setBounds(0,0, 430, 210);
        setLocationRelativeTo(null);
        setVisible(false);
        this.setResizable(false);
        initData();
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    private void initData(){
        new Thread(()->{
            try {
                List<Member> members = new HttpService().getMembers();
                setTitle("请选择成员");
                table.removeAll();
                if(members.isEmpty()){
                    JOptionPane.showMessageDialog(null, "你还没有添加任何成员","提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                for (Member member : members) {
                    String[] row = {member.getId().toString(), member.getName(), member.getIdCardNo()};
                    model.addRow(row);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "未知错误","提示", JOptionPane.PLAIN_MESSAGE);
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),"提示", JOptionPane.PLAIN_MESSAGE);
            }

        }).start();

    }
    private void submit(){
        if(table.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(this, "请选择接种成员","提示", JOptionPane.PLAIN_MESSAGE);
            return ;
        }
        Config.memberId = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
        success = true;
        this.dispose();
    }
    public boolean success(){
        return success;
    }
}

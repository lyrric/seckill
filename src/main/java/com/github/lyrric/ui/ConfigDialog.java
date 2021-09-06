package com.github.lyrric.ui;

import com.github.lyrric.conf.Config;
import com.github.lyrric.util.ParseUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 2020-07-23.
 * 设置微信的cookie
 * @author wangxiaodong
 */
public class ConfigDialog extends JDialog {

    JButton submit;

    JButton parse;
    JTextArea reqHeader;

    JTextField cookie;
    JTextField tk;

    Frame owner;

    boolean success = false;
    public ConfigDialog(Frame owner) {
        super(owner, true);
        this.owner = owner;
        init();
    }

    public void init(){
        this.setLayout(null);
        this.setTitle("请输入抓包的请求头.....");
        reqHeader = new JTextArea();
        reqHeader.setBounds(20,10,400,200);
        reqHeader.setText(Config.reqHeader);

        JLabel tkLabel = new JLabel("tk：");
        tkLabel.setBounds(10, 230, 60, 25);
        this.add(tkLabel);
        tk = new JTextField();
        tk.setBounds(70, 230, 350, 25);
        this.add(tk);

        JLabel cookieLabel = new JLabel("cookie：");
        cookieLabel.setBounds(10, 270, 60, 25);
        this.add(cookieLabel);
        cookie = new JTextField();
        cookie.setBounds(70, 270, 350, 25);
        this.add(cookie);


        submit = new JButton("保存");
        submit.setBounds(280, 320, 100, 40);
        submit.addActionListener(e -> {
            if(cookie.getText().isEmpty() || tk.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "请输入tk和cookie","提示", JOptionPane.PLAIN_MESSAGE);
            }else{
                Config.reqHeader = reqHeader.getText();
                Config.tk = tk.getText();
                calCookie(cookie.getText());
                success = true;
                this.dispose();
            }
        });

        parse = new JButton("解析");
        parse.setBounds(20, 320, 100, 40);
        parse.addActionListener(e -> {
            String[] data = ParseUtil.parseHeader(reqHeader.getText());
            if(data == null){
                JOptionPane.showMessageDialog(this, "数据格式错误","提示", JOptionPane.PLAIN_MESSAGE);
            }else{
                tk.setText(data[0]);
                cookie.setText(data[1]);
            }
        });
        this.add(parse);

        this.add(reqHeader);
        this.add(submit);
        this.setVisible(false);
        this.setBounds(500, 500, 460, 400);
        this.setResizable(false);
        setLocationRelativeTo(null);
    }

    public boolean success(){
        return success;
    }

    private void calCookie(String cookie){
        String[] s = cookie.replaceAll(" ", "").split(";");
        for (String s1 : s) {
            Config.cookie.put(s1.split("=")[0], s1);
        }
    }
}

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

    JTextArea jTextArea;

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
        jTextArea = new JTextArea();
        jTextArea.setBounds(20,10,400,300);
        jTextArea.setText(Config.reqHeader);
        submit = new JButton("验证并保存");
        submit.setBounds(180, 320, 100, 40);
        submit.addActionListener(e -> {
            if(jTextArea.getText().isEmpty() || !ParseUtil.parseHeader(jTextArea.getText())){
                JOptionPane.showMessageDialog(this, "数据格式错误或登录过期","提示", JOptionPane.PLAIN_MESSAGE);
            }else{
                success = true;
                this.dispose();
            }
        });
        this.add(jTextArea);
        this.add(submit);
        this.setVisible(false);
        this.setBounds(500, 500, 460, 400);
        setLocationRelativeTo(null);
    }

    public boolean success(){
        return success;
    }
}

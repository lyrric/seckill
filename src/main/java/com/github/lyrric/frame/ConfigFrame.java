package com.github.lyrric.frame;

import com.github.lyrric.conf.Config;
import com.github.lyrric.util.ParseUtil;

import javax.swing.*;

/**
 * Created on 2020-07-23.
 * 设置微信的cookie
 * @author wangxiaodong
 */
public class ConfigFrame extends JFrame {

    JButton submit;

    JTextArea jTextArea;

    public ConfigFrame(){
        this.setLayout(null);
        this.setTitle("请输入抓包的请求头.....");
        jTextArea = new JTextArea();
        jTextArea.setBounds(20,20,400,300);
        jTextArea.setText(Config.reqHeader);
        submit = new JButton("验证并保存");
        submit.setBounds(180, 330, 100, 40);
        submit.addActionListener(e -> {
            if(jTextArea.getText().isEmpty() || !ParseUtil.parseHeader(jTextArea.getText())){
                JOptionPane.showMessageDialog(null, "数据校验失败!","提示", JOptionPane.PLAIN_MESSAGE);
            }else{
                this.setVisible(false);
            }
        });
        this.add(jTextArea);
        this.add(submit);
        this.setVisible(false);
        this.setBounds(500, 500, 460, 420);
        setLocationRelativeTo(null);
    }


}

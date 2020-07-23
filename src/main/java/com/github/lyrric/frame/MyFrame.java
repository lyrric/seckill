package com.github.lyrric.frame;

import com.github.lyrric.model.BusinessException;
import com.github.lyrric.service.HttpService;
import com.github.lyrric.service.SecKillService;
import sun.misc.BASE64Decoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2020-07-21.
 *
 * @author wangxiaodong
 */
public class MyFrame extends JFrame {

    SecKillService service = new SecKillService();

    private String captureBase64;

    ConfigFrame configFrame ;
    JTextField code;
    JLabel jLabel;
    JButton checkSubmit;
    JLabel note;
    JButton analyseCode;
    JButton configButton;
    public MyFrame() throws HeadlessException {
        this.setLayout(null);
        this.setTitle("Just For Fun");
        configFrame = new ConfigFrame();
        jLabel = new JLabel("点击加载验证码");
        note = new JLabel();
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    String capture = service.getCapture();
                    captureBase64 = "data:image/png;base64," + capture;
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes = decoder.decodeBuffer(capture);
                    ImageIcon image = new ImageIcon(bytes);
                    jLabel.setIcon(image);
                    jLabel.setText("");
                    repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (BusinessException ex) {
                    note.setText(ex.getMessage());
                    repaint();
                }
            }
        });

        note.setBounds(50, 100,120,60);
        this.add(note);
        jLabel.setBounds(50, 20, 180, 70);
        code = new JTextField("");
        code.setBounds(250, 40, 100, 30);
        checkSubmit = new JButton("开始");
        checkSubmit.setBounds(360, 40, 100, 40);
        checkSubmit.addActionListener(e -> {
            service.startSecKill(code.getText(), 5352);
        });
        this.add(jLabel);
        this.add(code);
        this.add(checkSubmit);
        this.setBounds(500, 500, 500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        analyseCode = new JButton("识别验证码");
        analyseCode.setBounds(200, 100, 100, 30);
        analyseCode.addActionListener(e->{
            analyzeCapture();
        });
        this.add(analyseCode);
        configButton = new JButton("设置cookie");
        configButton.setBounds(320, 100, 100, 30);
        configButton.addActionListener((e)->{
            configFrame.setVisible(true);
        });
        this.add(configButton);
        this.setVisible(true);
    }

    @Override
    public void update(Graphics g) {

        super.update(g);
    }

    @Override
    public void repaint(long time, int x, int y, int width, int height) {
        super.repaint(time, x, y, width, height);
    }

    private void analyzeCapture()  {
        try {
            String result = new HttpService().getCode(captureBase64);
            code.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"提示", JOptionPane.PLAIN_MESSAGE);
        }
    }
}

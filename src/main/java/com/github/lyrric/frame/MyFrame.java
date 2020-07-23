package com.github.lyrric.frame;

import com.github.lyrric.model.BusinessException;
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

    SecKillService service = new SecKillService(
            "_xzkj_=wxtoken:e0963da6b3e544f4613e8ba2c1df6fe0_af28fd895718e6daf58659670cdddde4; _xxhm_=%7B%22address%22%3A%22%22%2C%22awardPoints%22%3A0%2C%22birthday%22%3A835545600000%2C%22createTime%22%3A1574304016000%2C%22headerImg%22%3A%22http%3A%2F%2Fthirdwx.qlogo.cn%2Fmmopen%2FdH8QVxmk2IXORh7FiapbUSZd3qotRsSWktOKjqSI3ibtrP1u6Zf3PQqc84b8PGcHibW76M6zLmnosib9KQeaeYzvSrCliaKAXEXcq%2F132%22%2C%22id%22%3A3926372%2C%22idCardNo%22%3A%22510727199606244528%22%2C%22isRegisterHistory%22%3A0%2C%22latitude%22%3A30.587389%2C%22longitude%22%3A104.06224%2C%22mobile%22%3A%2218608283793%22%2C%22modifyTime%22%3A1593757208000%2C%22name%22%3A%22%E9%99%88%E6%9F%B3%E9%9D%92%22%2C%22nickName%22%3A%22lyrric%22%2C%22openId%22%3A%22oWzsq52mreJ9_E_f2R0QSvwlQl8M%22%2C%22regionCode%22%3A%22510107%22%2C%22registerTime%22%3A1593757208000%2C%22sex%22%3A2%2C%22source%22%3A1%2C%22uFrom%22%3A%22cdbdbsy%22%2C%22unionid%22%3A%22oiGJM6PFEuP1AJ1jmx91bbcjBzmY%22%2C%22wxSubscribed%22%3A1%2C%22yn%22%3A1%7D; UM_distinctid=1737a3648e550f-04c40e81e605af-8011274-1fa400-1737a3648e7462; CNZZDATA1261985103=296461450-1595478263-https%253A%252F%252Fopen.weixin.qq.com%252F%7C1595478263",
            "1b66012f594bf994cfa0a0b2175bfb9d",
            "wxtoken:e0963da6b3e544f4613e8ba2c1df6fe0_af28fd895718e6daf58659670cdddde4");

    JTextField code;
    JLabel jLabel;
    JButton checkSubmit;

    JLabel note;
    public MyFrame() throws HeadlessException {
        this.setLayout(null);
        jLabel = new JLabel("点击加载验证码");
        note = new JLabel();
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    String capture = service.getCapture();
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
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
}

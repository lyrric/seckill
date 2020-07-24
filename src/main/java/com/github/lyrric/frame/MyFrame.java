package com.github.lyrric.frame;

import com.github.lyrric.conf.Config;
import com.github.lyrric.model.BusinessException;
import com.github.lyrric.model.VaccineList;
import com.github.lyrric.service.SecKillService;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020-07-21.
 *
 * @author wangxiaodong
 */
public class MyFrame extends JFrame {

    SecKillService service = new SecKillService();

    /**
     * 验证码64
     */
    private String captureBase64;
    /**
     * 疫苗列表
     */
    private List<VaccineList> vaccines;


    ConfigFrame configFrame ;
    JTextField codeField;
    JLabel codeImage;
    JButton startBtn;
    JButton configBtn;
    JTable vaccinesTable;

    JButton refreshBtn;
    DefaultTableModel tableModel;

    JTextArea note;
    public MyFrame() {
        setLayout(null);
        setTitle("Just For Fun");
        setBounds(500 , 500, 540, 360);
        init();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void init(){
        configFrame = new ConfigFrame();
        codeImage = new JLabel("点击加载验证码");
        codeImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                refreshImage();
            }
        });
        codeField = new JTextField("");
        startBtn = new JButton("开始");

        startBtn.addActionListener(e -> {
           start();
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configBtn = new JButton("设置cookie");
        configBtn.addActionListener((e)->{
            configFrame.setVisible(true);
        });
        refreshBtn = new JButton("刷新疫苗列表");
        refreshBtn.addActionListener((e)->{
            refreshVaccines();
        });

        note = new JTextArea();
        note.append("日记记录：\r\n");
        note.setEnabled(false);

        String[] columnNames = { "id", "医院名称","秒杀时间" };
        tableModel = new VaccineTableModel(new String[0][], columnNames);
        vaccinesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vaccinesTable);

        scrollPane.setBounds(10,10,360,200);

        codeImage.setBounds(20, 225, 100, 40);
        codeField.setBounds(180, 230, 60, 30);
        startBtn.setBounds(260, 230, 100, 30);

        configBtn.setBounds(20, 280, 100, 30);
        refreshBtn.setBounds(130, 280,120, 30);
        note.setBounds(380, 10, 120, 300);

        add(note);
        add(scrollPane);
        add(codeImage);
        add(codeField);
        add(startBtn);
        add(configBtn);
        add(refreshBtn);
    }



    private void refreshVaccines(){
        try {
            vaccines = service.getVaccines();
            vaccinesTable.removeAll();
            if(vaccines != null && !vaccines.isEmpty()){
                for (VaccineList hospital : vaccines) {
                    List<VaccineList.Vaccine> list = hospital.getVaccines();
                    for (VaccineList.Vaccine t : list) {
                        String[] item = { t.getId().toString(), hospital.getName(),t.getSubDateStart() };
                        tableModel.addRow(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            appendMsg("未知错误");
        } catch (BusinessException e) {
            appendMsg("错误："+e.getErrMsg()+"，errCode"+e.getCode());
        }
    }
    private void start(){
        if(StringUtils.isEmpty(Config.cookies)){
            appendMsg("请配置cookie!!!");
            return ;
        }
        if(vaccinesTable.getSelectedRow() < 0){
            appendMsg("请选择要抢购的疫苗");
            return ;
        }
        if(StringUtils.isEmpty(codeField.getText())){
            appendMsg("请先输入验证码");
            return ;
        }
        Integer id = Integer.parseInt(tableModel.getValueAt(vaccinesTable.getSelectedRow(), 0).toString());
        service.startSecKill(codeField.getText(), id);
    }
    private void refreshImage(){
        try {
            captureBase64  = service.getCapture();
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(captureBase64);
            ImageIcon image = new ImageIcon(bytes);
            ImageIcon imageIcon = new ImageIcon(image.getImage().getScaledInstance(100, 50, Image.SCALE_DEFAULT));
            codeImage.setIcon(imageIcon);
            codeImage.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BusinessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),"提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void appendMsg(String message){
        note.append(message);
        note.append("\r\n");
    }
}

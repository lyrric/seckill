package com.github.lyrric;

import com.github.lyrric.ui.MainFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created on 2020-07-21.
 *
 * @author wangxiaodong
 */
public class Main {

    private  static  final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=================程序开始运行=================");
        new MainFrame();
    }

}

import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.lang.util.Disks;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by xiepeilong on 2018/1/12.
 */
public class MuMu {

    static Log log = Logs.get();

    /**
     * 截取屏幕图片进行OCR识别
     *
     * @param folder 图片保存路径
     * @throws Exception
     */
    public static String captureScreen(String folder) throws Exception {

        // 获取名目尺寸
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Rectangle screenRectangle = new Rectangle(screenSize);

        Rectangle screenRectangle = new Rectangle(30, 150, 500, 400);

        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);

        String imageName = R.captchaChar(6);

        String fileName = Disks.normalize(folder + "/" + imageName + ".png");

        String analysisFile = Disks.normalize(folder + "/" + imageName);

        File file = Files.createFileIfNoExists(new File(fileName));

        boolean result = ImageIO.write(image, "png", file);

        if (result) {
            String read = doOcr(fileName, analysisFile);
            log.infof("解析问题 %s", read);
            return read;
        }

        return "";
    }

    public static String captureScreen() {
        try {
            return captureScreen("~/hero");
        } catch (Exception e) {
            e.printStackTrace();
            log.errorf("获取图片失败 %s", e.getMessage());
            return null;
        }
    }


    public static String doOcr(String fileName, String analysisFile) throws Exception {

        StringBuilder terr = new StringBuilder();
        StringBuilder tout = new StringBuilder();
        Lang.exec(new String[]{"C:/Program Files (x86)/Tesseract-OCR/tesseract.exe",
                fileName,
                analysisFile, "-l", "chi_sim"}, tout, terr);

        log.errorf("发生异常 %s", terr.toString());
        log.infof("正确处理 %s", tout.toString());

        String read = Files.read(analysisFile + ".txt");

        return read;
    }

    public static void main(String[] args) {
        try {
            captureScreen("~/hero");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

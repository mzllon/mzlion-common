import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * Created by mzlion on 2016/6/5.
 */
public class FileUtilsTest {

    @Test
    public void test() throws Exception {
        File srcDir = new File("e:/utilities");
        File destDir = new File("e:/utilities_1");
        FileUtils.copyDirectory(srcDir, destDir);
    }
}

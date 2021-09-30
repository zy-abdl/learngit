import java.io.*;

public class yq {
    public static void main(String[] args) throws IOException {
        // 构建FileInputStream对象
        FileInputStream fip = new FileInputStream(new File("src/yq_in.txt"));
        // 构建InputStreamReader对象
        InputStreamReader reader = new InputStreamReader(fip, "GBK");

        StringBuffer sb = new StringBuffer();
        while (reader.ready()) {
            // 转成char加到StringBuffer对象中
            sb.append((char) reader.read());
        }

        String str = sb.toString();
        String[] str1 = str.split("\\s+");
        int strlength = str1.length;

        // 构建FileOutputStream对象,文件不存在会自动新建
        FileOutputStream fop = new FileOutputStream(new File("src/yq_out.txt"));
        // 构建OutputStreamWriter对象,windows上默认编码是gbk
        OutputStreamWriter writer = new OutputStreamWriter(fop, "GBK");

        String province = "";
        for (int i = 0;i < strlength;i++)
        {
            if(str1[i].equals(province)) i++;
            else
            {
                province = str1[i];
                if(i == 0) writer.append(str1[i]+"\n");
                else writer.append("\n"+str1[i]+"\n");
                i++;
            }
            //去除待明确地区
            if(str1[i].equals("待明确地区")) i++;
            else {
                writer.append(str1[i] + "\t");
                i++;
                writer.append(str1[i] + "\n");
            }
        }

        writer.close();        // 关闭写入流,同时会把缓冲区内容写入文件

        fop.close();           // 关闭输出流,释放系统资源

        reader.close();        // 关闭读取流

        fip.close();           // 关闭输入流,释放系统资源

    }
}
